package vn.edu.huit.diengiadung.service;

import vn.edu.huit.diengiadung.entity.NhaCungCap;

import java.util.List;

public interface NhaCungCapService {
    List<NhaCungCap> layDanhSach();
    List<NhaCungCap> timKiem(String tuKhoa);
    NhaCungCap layTheoId(Long id);
    NhaCungCap luu(NhaCungCap nhaCungCap);
    void xoa(Long id);
}
