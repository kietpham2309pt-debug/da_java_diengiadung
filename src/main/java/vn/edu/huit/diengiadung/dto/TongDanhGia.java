package vn.edu.huit.diengiadung.model;

public class TongDanhGia {

    private final long[] theoSao = new long[6];

    public void cong(int soSao, long soLuong) {
        if (soSao >= 1 && soSao <= 5) {
            theoSao[soSao] += soLuong;
        }
    }

    public long getTongSo() {
        long tong = 0;
        for (int sao = 1; sao <= 5; sao++) {
            tong += theoSao[sao];
        }
        return tong;
    }

    public double getTrungBinh() {
        long tong = getTongSo();
        if (tong == 0) {
            return 0;
        }
        long diem = 0;
        for (int sao = 1; sao <= 5; sao++) {
            diem += sao * theoSao[sao];
        }
        return Math.round(diem * 10.0 / tong) / 10.0;
    }

    public long soLuong(int soSao) {
        return theoSao[soSao];
    }

    public int phanTram(int soSao) {
        long tong = getTongSo();
        return tong == 0 ? 0 : (int) Math.round(theoSao[soSao] * 100.0 / tong);
    }
}
