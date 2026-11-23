(function () {
    'use strict';

    var form = document.getElementById('dt-form');
    if (!form) return;
    var nut = document.getElementById('dt-chay');
    var vung = document.getElementById('dt-ket-qua');
    var tl = document.getElementById('dt-tl');

    ['soKhach', 'tonBanDau'].forEach(function (ten) {
        var o = document.getElementById(ten);
        var gt = document.getElementById(ten + '-gt');
        o.addEventListener('input', function () { gt.textContent = o.value; });
    });

    function tao(the, lop, chu) {
        var e = document.createElement(the);
        if (lop) e.className = lop;
        if (chu != null) e.textContent = chu;
        return e;
    }

    function ve(kq) {
        vung.hidden = false;
        document.getElementById('dt-so-khach').textContent = kq.soKhach;
        document.getElementById('dt-ton-dau').textContent = kq.tonBanDau;
        document.getElementById('dt-thanh-cong').textContent = kq.thanhCong;
        document.getElementById('dt-het-hang').textContent = kq.hetHang;
        document.getElementById('dt-ton-cuoi').textContent = kq.tonCuoi;
        document.getElementById('dt-tong-ms').textContent = '(tổng ' + kq.tongMs + ' ms)';

        var dung = kq.thanhCong <= kq.tonBanDau && kq.tonCuoi === kq.tonBanDau - kq.thanhCong;
        var kl = document.getElementById('dt-ket-luan');
        kl.className = 'dt-ket-luan ' + (dung ? 'dt-ket-luan--dung' : 'dt-ket-luan--sai');
        if (dung) {
            kl.textContent = 'ĐÚNG: ' + kq.thanhCong + ' khách mua được đúng bằng số tồn, ' + kq.hetHang
                + ' khách còn lại được báo hết hàng, tồn cuối = ' + kq.tonCuoi + '. Không bán vượt.';
        } else {
            var vuot = Math.max(0, kq.thanhCong - kq.tonBanDau);
            kl.textContent = 'SAI: ' + kq.thanhCong + ' khách "mua được" trong khi chỉ có ' + kq.tonBanDau
                + ' sản phẩm' + (vuot ? ' (bán vượt ' + vuot + ')' : '') + ', vậy mà tồn cuối lại ghi ' + kq.tonCuoi
                + '. Các luồng cùng đọc một số tồn cũ rồi ghi đè lên nhau nên mất cập nhật.';
        }

        tl.textContent = '';
        var max = Math.max.apply(null, kq.dongThoiGian.map(function (d) { return d.ketThucMs; }).concat([1]));
        kq.dongThoiGian.forEach(function (d) {
            var hang = tao('div', 'dt-hang');
            hang.appendChild(tao('span', 'dt-ten', d.luong));
            var ray = tao('div', 'dt-ray');
            var thanh = tao('div', 'dt-thanh ' + (d.thanhCong ? 'dt-thanh--ok' : 'dt-thanh--het'));
            thanh.style.left = (d.batDauMs / max * 100) + '%';
            thanh.style.width = Math.max(1.2, (d.ketThucMs - d.batDauMs) / max * 100) + '%';
            thanh.title = d.luong + ': ' + d.batDauMs + ' → ' + d.ketThucMs + ' ms';
            ray.appendChild(thanh);
            hang.appendChild(ray);
            hang.appendChild(tao('span', 'dt-ghi-chu', d.ketThucMs + ' ms · ' + d.ghiChu));
            tl.appendChild(hang);
        });
        vung.scrollIntoView({behavior: 'smooth', block: 'start'});
    }

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        nut.disabled = true;
        nut.textContent = 'Đang chạy...';
        fetch(form.action, {
            method: 'POST',
            credentials: 'same-origin',
            headers: {'Accept': 'application/json'},
            body: new URLSearchParams(new FormData(form))
        }).then(function (r) {
            return r.json().then(function (dl) { return {ok: r.ok, dl: dl}; });
        }).then(function (kq) {
            if (!kq.ok) throw new Error(kq.dl.thongBao || 'Không chạy được');
            ve(kq.dl);
        }).catch(function (loi) {
            vung.hidden = false;
            var kl = document.getElementById('dt-ket-luan');
            kl.className = 'dt-ket-luan dt-ket-luan--sai';
            kl.textContent = loi.message;
        }).finally(function () {
            nut.disabled = false;
            nut.textContent = 'Chạy thử';
        });
    });
})();
