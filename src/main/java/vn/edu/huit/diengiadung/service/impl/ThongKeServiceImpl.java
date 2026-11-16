package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.repository.ThongKeDao;
import vn.edu.huit.diengiadung.service.ThongKeSanPham;
import vn.edu.huit.diengiadung.service.ThongKeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThongKeServiceImpl implements ThongKeService {

    private static final int SO_SAN_PHAM_BAN_CHAY = 10;

    private final ThongKeDao thongKeDao;

    public ThongKeServiceImpl(ThongKeDao thongKeDao) {
        this.thongKeDao = thongKeDao;
    }

    @Override
    public long tinhDoanhThu(int thang, int nam) {
        return thongKeDao.tongDoanhThu(dauThang(thang, nam), dauThang(thang, nam).plusMonths(1));
    }

    @Override
    public long demDonHoanThanh(int thang, int nam) {
        return thongKeDao.demDonHoanThanh(dauThang(thang, nam), dauThang(thang, nam).plusMonths(1));
    }

    @Override
    public List<ThongKeSanPham> laySanPhamBanChay(int thang, int nam) {
        return thongKeDao.sanPhamBanChay(dauThang(thang, nam), dauThang(thang, nam).plusMonths(1), SO_SAN_PHAM_BAN_CHAY);
    }

    private static LocalDateTime dauThang(int thang, int nam) {
        return LocalDate.of(nam, thang, 1).atStartOfDay();
    }
}
