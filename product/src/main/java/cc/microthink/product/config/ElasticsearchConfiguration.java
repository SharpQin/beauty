package cc.microthink.product.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.http.HttpHeaders;

import java.time.*;
import java.util.Arrays;

@Configuration
public class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("header_key", "header_value");

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
            .connectedTo("localhost:9200")  //, ""
            //.usingSsl()
            //.withProxy("localhost:8888")
            .withConnectTimeout(Duration.ofSeconds(5)) //Default is 10 sec.
            .withSocketTimeout(Duration.ofSeconds(3))  //Default is 5 sec.
            .withDefaultHeaders(httpHeaders)
            //.withBasicAuth("elastic_user","elastic_pwd")
            .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(
            Arrays.asList(
                new ZonedDateTimeWritingConverter(),
                new ZonedDateTimeReadingConverter(),
                new InstantWritingConverter(),
                new InstantReadingConverter(),
                new LocalDateWritingConverter(),
                new LocalDateReadingConverter()
            )
        );
    }

    @WritingConverter
    static class ZonedDateTimeWritingConverter implements Converter<ZonedDateTime, String> {

        @Override
        public String convert(ZonedDateTime source) {
            if (source == null) {
                return null;
            }
            return source.toInstant().toString();
        }
    }

    @ReadingConverter
    static class ZonedDateTimeReadingConverter implements Converter<String, ZonedDateTime> {

        @Override
        public ZonedDateTime convert(String source) {
            if (source == null) {
                return null;
            }
            return Instant.parse(source).atZone(ZoneId.systemDefault());
        }
    }

    @WritingConverter
    static class InstantWritingConverter implements Converter<Instant, String> {

        @Override
        public String convert(Instant source) {
            if (source == null) {
                return null;
            }
            return source.toString();
        }
    }

    @ReadingConverter
    static class InstantReadingConverter implements Converter<String, Instant> {

        @Override
        public Instant convert(String source) {
            if (source == null) {
                return null;
            }
            return Instant.parse(source);
        }
    }

    @WritingConverter
    static class LocalDateWritingConverter implements Converter<LocalDate, String> {

        @Override
        public String convert(LocalDate source) {
            if (source == null) {
                return null;
            }
            return source.toString();
        }
    }

    @ReadingConverter
    static class LocalDateReadingConverter implements Converter<String, LocalDate> {

        @Override
        public LocalDate convert(String source) {
            if (source == null) {
                return null;
            }
            return LocalDate.parse(source);
        }
    }
}
