package dao;

import java.util.List;

import domain.PlayList;

public interface IAdaptadorPlayListDAO {
	public void registrarPlayList(PlayList playList);
	public void borrarPlayList(PlayList playList);
	public void modificarPlayList(PlayList playList);
	public PlayList recuperarPlayList(int codigo);
	public List<PlayList> recuperarTodasPlayLists();
}
