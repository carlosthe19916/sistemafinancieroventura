package ventura.org.hibernate.dialect;

import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.dialect.pagination.LimitHelper;
import org.hibernate.engine.spi.RowSelection;

/**
 * LIMIT clause handler compatible with ISO and ANSI SQL:2008 standard.
 * 
 * @author zhouyanming (zhouyanming@gmail.com)
 */
public class SQL2008StandardLimitHandler extends AbstractLimitHandler {

	/**
	 * Constructs a SQL2008StandardLimitHandler
	 * 
	 * @param sql
	 *            The SQL
	 * @param selection
	 *            The row selection options
	 */
	public SQL2008StandardLimitHandler(String sql, RowSelection selection) {
		super(sql, selection);
	}

	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public String getProcessedSql() {
		if (LimitHelper.useLimit(this, selection)) {
			return sql
					+ (LimitHelper.hasFirstRow(selection) ? " offset ? rows fetch next ? rows only"
							: " fetch first ? rows only");
		} else {
			// or return unaltered SQL
			return sql;
		}
	}

}
