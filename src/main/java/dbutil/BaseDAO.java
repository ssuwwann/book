package dbutil;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDAO {
	protected static DataSource dataSource;

	static {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("dbcp");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	protected Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	protected void closeAll(ResultSet rs, PreparedStatement pstmt) {
		if (rs != null) {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
