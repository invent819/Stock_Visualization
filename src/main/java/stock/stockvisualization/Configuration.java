package stock.stockvisualization;

import javax.sql.DataSource;

@org.springframework.context.annotation.Configuration
public class Configuration {
    private final DataSource dataSource;

    public Configuration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
