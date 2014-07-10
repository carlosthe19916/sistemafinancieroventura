package ventura.org.hibernate.dialect;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.engine.spi.RowSelection;
/**
 * An SQL dialect for Oracle 12c.
 * 
 * @author zhouyanming (zhouyanming@gmail.com)
 */

/**
 * An SQL dialect for Oracle 12c.
 * 
 * @author zhouyanming (zhouyanming@gmail.com)
 */
public class Oracle12cDialect extends Oracle10gDialect {

    public Oracle12cDialect() {
            super();
    }
    
    @Override
	protected void registerDefaultProperties() {
		super.registerDefaultProperties();
		getDefaultProperties().setProperty( Environment.USE_GET_GENERATED_KEYS, "true" );
	}
    
    @Override
	public boolean supportsIdentityColumns() {
		return true;
	}

	@Override
	public boolean supportsInsertSelectIdentity() {
		return true;
	}

	@Override
	public String getIdentityColumnString() {
		return "generated as identity";
	}

    @Override
    public LimitHandler buildLimitHandler(String sql, RowSelection selection) {
            return new SQL2008StandardLimitHandler(sql, selection);
    }

}
