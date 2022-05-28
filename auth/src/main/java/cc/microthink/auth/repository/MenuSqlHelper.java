package cc.microthink.auth.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class MenuSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("link", table, columnPrefix + "_link"));
        columns.add(Column.aliased("method", table, columnPrefix + "_method"));
        columns.add(Column.aliased("auth_key", table, columnPrefix + "_auth_key"));
        columns.add(Column.aliased("showed", table, columnPrefix + "_showed"));
        columns.add(Column.aliased("parent", table, columnPrefix + "_parent"));

        return columns;
    }
}
