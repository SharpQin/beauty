package cc.microthink.auth.repository.rowmapper;

import cc.microthink.auth.domain.Authority;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Authority}, with proper type conversions.
 */
@Service
public class AuthorityRowMapper implements BiFunction<Row, String, Authority> {

    private final ColumnConverter converter;

    public AuthorityRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Authority} stored in the database.
     */
    @Override
    public Authority apply(Row row, String prefix) {
        Authority entity = new Authority();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setLink(converter.fromRow(row, prefix + "_link", String.class));
        entity.setMethod(converter.fromRow(row, prefix + "_method", String.class));
        entity.setAuthKey(converter.fromRow(row, prefix + "_auth_key", String.class));
        entity.setShowed(converter.fromRow(row, prefix + "_showed", Boolean.class));
        entity.setParent(converter.fromRow(row, prefix + "_parent", Long.class));
        return entity;
    }
}
