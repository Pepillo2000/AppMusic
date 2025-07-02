package dao;

public class TDSFactoriaDAO extends FactoriaDAO {

	public TDSFactoriaDAO() {}

	@Override
	public AdaptadorUsuarioTDS getUsuarioDAO() {
		return AdaptadorUsuarioTDS.getUnicaInstancia();
	}
	
	@Override
	public AdaptadorCancionTDS getCancionDAO() {
		return AdaptadorCancionTDS.getUnicaInstancia();
	}

	@Override
	public AdaptadorPlayListTDS getPlayListDAO() {
		return AdaptadorPlayListTDS.getUnicaInstancia();
	}

}
