package dao;

public abstract class FactoriaDAO {

	private static FactoriaDAO unicaInstancia;

	public static final String DAO_TDS = "dao.TDSFactoriaDAO"; // actualizada esta linea

	/**
	 * Crea un tipo de factoria DAO. Solo existe el tipo TDSFactoriaDAO
	 */
	public static FactoriaDAO getInstancia(String tipo) throws DAOException {
		if (unicaInstancia == null)
			try {
				unicaInstancia = (FactoriaDAO) Class.forName(tipo).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new DAOException(e.getMessage());
			}
		return unicaInstancia;
	}
	
	public static FactoriaDAO getInstancia() throws DAOException {
		if (unicaInstancia == null)
			return getInstancia(FactoriaDAO.DAO_TDS);
		else
			return unicaInstancia;
	}

	protected FactoriaDAO() {
	}

	// Metodos factoria para obtener adaptadores

	public abstract AdaptadorUsuarioTDS getUsuarioDAO();
	public abstract AdaptadorCancionTDS getCancionDAO();
	public abstract AdaptadorPlayListTDS getPlayListDAO();
}