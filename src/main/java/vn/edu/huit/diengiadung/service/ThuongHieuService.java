package vn.edu.huit.diengiadung.service;

import vn.edu.huit.diengiadung.entity.ThuongHieu;
import java.util.List;

public interface ThuongHieuService {

    List<ThuongHieu> layDanhSach();

    List<ThuongHieu> timKiem(String tuKhoa);

    ThuongHieu layTheoId(Long id);

    ThuongHieu luu(ThuongHieu thuongHieu);

    void xoa(Long id);
}