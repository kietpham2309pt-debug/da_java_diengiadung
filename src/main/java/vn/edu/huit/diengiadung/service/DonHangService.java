package vn.edu.huit.diengiadung.service;

import vn.edu.huit.diengiadung.entity.DonHang;

import java.util.List;

public interface DonHangService {

    List<DonHang> layTatCa();

    List<DonHang> timTheoTrangThai(String trangThai);

    DonHang layTheoId(Long id);
}