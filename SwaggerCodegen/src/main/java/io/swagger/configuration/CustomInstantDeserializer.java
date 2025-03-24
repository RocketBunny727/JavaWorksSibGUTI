package io.swagger.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.datatype.jsr310.DecimalUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Custom deserializer for {@link Instant}, {@link OffsetDateTime}, and {@link ZonedDateTime}.
 * Supports RFC 822 format.
 */
public class CustomInstantDeserializer<T extends Temporal> extends StdScalarDeserializer<T> {
  private static final long serialVersionUID = 1L;

  private final DateTimeFormatter formatter;
  protected final Function<FromIntegerArguments, T> fromMilliseconds;
  protected final Function<FromDecimalArguments, T> fromNanoseconds;
  protected final Function<TemporalAccessor, T> parsedToValue;
  protected final BiFunction<T, ZoneId, T> adjust;

  public static final CustomInstantDeserializer<Instant> INSTANT = new CustomInstantDeserializer<>(
          Instant.class, DateTimeFormatter.ISO_INSTANT,
          Instant::from,
          args -> Instant.ofEpochMilli(args.value),
          args -> Instant.ofEpochSecond(args.integer, args.fraction),
          null
  );

  public static final CustomInstantDeserializer<OffsetDateTime> OFFSET_DATE_TIME = new CustomInstantDeserializer<>(
          OffsetDateTime.class, DateTimeFormatter.ISO_OFFSET_DATE_TIME,
          OffsetDateTime::from,
          args -> OffsetDateTime.ofInstant(Instant.ofEpochMilli(args.value), args.zoneId),
          args -> OffsetDateTime.ofInstant(Instant.ofEpochSecond(args.integer, args.fraction), args.zoneId),
          (dateTime, zone) -> dateTime.withOffsetSameInstant(zone.getRules().getOffset(dateTime.toLocalDateTime()))
  );

  public static final CustomInstantDeserializer<ZonedDateTime> ZONED_DATE_TIME = new CustomInstantDeserializer<>(
          ZonedDateTime.class, DateTimeFormatter.ISO_ZONED_DATE_TIME,
          ZonedDateTime::from,
          args -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(args.value), args.zoneId),
          args -> ZonedDateTime.ofInstant(Instant.ofEpochSecond(args.integer, args.fraction), args.zoneId),
          ZonedDateTime::withZoneSameInstant
  );

  protected CustomInstantDeserializer(Class<T> supportedType,
                                      DateTimeFormatter formatter,
                                      Function<TemporalAccessor, T> parsedToValue,
                                      Function<FromIntegerArguments, T> fromMilliseconds,
                                      Function<FromDecimalArguments, T> fromNanoseconds,
                                      BiFunction<T, ZoneId, T> adjust) {
    super(supportedType);
    this.formatter = formatter;
    this.parsedToValue = parsedToValue;
    this.fromMilliseconds = fromMilliseconds;
    this.fromNanoseconds = fromNanoseconds;
    this.adjust = adjust != null ? adjust : (t, zoneId) -> t;
  }

  @Override
  public T deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    if (parser.hasToken(JsonToken.VALUE_NUMBER_FLOAT)) {
      BigDecimal value = parser.getDecimalValue();
      long seconds = value.longValue();
      int nanoseconds = DecimalUtils.extractNanosecondDecimal(value, seconds);
      return fromNanoseconds.apply(new FromDecimalArguments(seconds, nanoseconds, getZone(context)));
    }
    if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
      long timestamp = parser.getLongValue();
      return context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
              ? fromNanoseconds.apply(new FromDecimalArguments(timestamp, 0, getZone(context)))
              : fromMilliseconds.apply(new FromIntegerArguments(timestamp, getZone(context)));
    }
    if (parser.hasToken(JsonToken.VALUE_STRING)) {
      String string = parser.getText().trim();
      if (string.isEmpty()) {
        return null;
      }
      if (string.endsWith("+0000")) {
        string = string.substring(0, string.length() - 5) + "Z";
      }
      try {
        TemporalAccessor accessor = formatter.parse(string);
        T valueParsed = parsedToValue.apply(accessor);
        return context.isEnabled(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                ? adjust.apply(valueParsed, getZone(context))
                : valueParsed;
      } catch (DateTimeException e) {
        throw new IOException("Error parsing date", e);
      }
    }
    throw new IOException("Expected type float, integer, or string.");
  }

  private ZoneId getZone(DeserializationContext context) {
    return (handledType() == Instant.class) ? ZoneOffset.UTC : context.getTimeZone().toZoneId();
  }

  private static class FromIntegerArguments {
    public final long value;
    public final ZoneId zoneId;

    private FromIntegerArguments(long value, ZoneId zoneId) {
      this.value = value;
      this.zoneId = zoneId;
    }
  }

  private static class FromDecimalArguments {
    public final long integer;
    public final int fraction;
    public final ZoneId zoneId;

    private FromDecimalArguments(long integer, int fraction, ZoneId zoneId) {
      this.integer = integer;
      this.fraction = fraction;
      this.zoneId = zoneId;
    }
  }
}