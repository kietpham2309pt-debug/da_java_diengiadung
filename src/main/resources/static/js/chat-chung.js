(function () {
    'use strict';

    function tao(the, lop, chu) {
        var e = document.createElement(the);
        if (lop) e.className = lop;
        if (chu != null) e.textContent = chu;
        return e;
    }

    function chenLienKet(khung, chu) {
        var mau = /(\/(?:don-hang|san-pham|gio-hang|ho-so|yeu-thich)[\w\-\/?=&.]*)/g;
        var cuoi = 0, m;
        while ((m = mau.exec(chu)) !== null) {
            if (m.index > cuoi) khung.appendChild(document.createTextNode(chu.slice(cuoi, m.index)));
            var a = tao('a', null, m[1]);
            a.href = m[1];
            khung.appendChild(a);
            cuoi = m.index + m[1].length;
        }
        if (cuoi < chu.length) khung.appendChild(document.createTextNode(chu.slice(cuoi)));
    }

    function gio(iso) {
        return iso ? iso.substring(11, 16) : '';
    }

    function nhanNgay(iso) {
        var d = new Date(iso.substring(0, 10) + 'T00:00:00');
        var homNay = new Date();
        homNay.setHours(0, 0, 0, 0);
        var lech = Math.round((homNay - d) / 86400000);
        if (lech === 0) return 'Hôm nay';
        if (lech === 1) return 'Hôm qua';
        return iso.substring(8, 10) + '/' + iso.substring(5, 7) + '/' + iso.substring(0, 4);
    }

    function chuDau(ten) {
        if (!ten) return '?';
        var tu = ten.trim().split(/\s+/);
        return tu[tu.length - 1].charAt(0).toUpperCase();
    }

    function BoVe(than, tuyChon) {
        this.than = than;
        this.laMinh = tuyChon.laMinh;
        this.anh = tuyChon.anh;
        this.hienTen = tuyChon.hienTen !== false;
        this.lamLienKet = tuyChon.lamLienKet || function () { return false; };
        this.xoa();
    }

    BoVe.prototype.xoa = function () {
        this.than.textContent = '';
        this.ngayCuoi = null;
        this.nhomCuoi = null;
        this.dongCuoi = null;
        this.thoiCuoi = 0;
        this.daXem = null;
    };

    BoVe.prototype.them = function (tin) {
        var ngay = tin.thoiGian.substring(0, 10);
        if (ngay !== this.ngayCuoi) {
            this.than.appendChild(tao('div', 'cb-ngay', nhanNgay(tin.thoiGian)));
            this.ngayCuoi = ngay;
            this.nhomCuoi = null;
        }
        if (tin.nguoiGui === 'HE_THONG') {
            this.than.appendChild(tao('div', 'cb-he-thong', tin.noiDung));
            this.nhomCuoi = null;
            this.cuon();
            return null;
        }
        var minh = this.laMinh(tin);
        var khoaNhom = tin.nguoiGui + '|' + (tin.tenNguoiGui || '');
        var thoi = Date.parse(tin.thoiGian);
        var noiTiep = this.nhomCuoi === khoaNhom && thoi - this.thoiCuoi < 5 * 60000;
        var dong = tao('div', 'cb-dong' + (minh ? ' cb-dong--minh' : '') + (noiTiep ? '' : ' cb-dong--dau'));
        dong.dataset.id = tin.id;
        if (!minh) {
            var oAnh = tao('div', 'cb-dong__anh');
            if (!noiTiep) {
                var a = this.anh(tin);
                oAnh.appendChild(tao('span', 'cb-anh cb-anh--nho' + (a.lop ? ' ' + a.lop : ''), a.chu));
            }
            dong.appendChild(oAnh);
        }
        var cot = tao('div', 'cb-dong__cot');
        if (!minh && !noiTiep && this.hienTen && tin.tenNguoiGui) {
            cot.appendChild(tao('div', 'cb-ten', tin.tenNguoiGui));
        }
        var bong = tao('div', 'cb-tin');
        bong.title = gio(tin.thoiGian);
        if (this.lamLienKet(tin)) chenLienKet(bong, tin.noiDung); else bong.textContent = tin.noiDung;
        cot.appendChild(bong);
        if (this.dongCuoi && this.nhomCuoi === khoaNhom && noiTiep) {
            var gioCu = this.dongCuoi.querySelector('.cb-gio');
            if (gioCu) gioCu.remove();
        }
        cot.appendChild(tao('div', 'cb-gio', gio(tin.thoiGian)));
        dong.appendChild(cot);
        this.than.appendChild(dong);
        this.nhomCuoi = khoaNhom;
        this.thoiCuoi = thoi;
        this.dongCuoi = dong;
        if (minh && this.daXem) {
            this.daXem.remove();
            this.daXem = null;
        }
        this.cuon();
        return dong;
    };

    BoVe.prototype.danhDauDaXem = function () {
        var cacDong = this.than.querySelectorAll('.cb-dong--minh');
        if (!cacDong.length) return;
        if (this.daXem) this.daXem.remove();
        var cuoi = cacDong[cacDong.length - 1].querySelector('.cb-dong__cot');
        this.daXem = tao('div', 'cb-da-xem', '✓✓ Đã xem');
        cuoi.appendChild(this.daXem);
        this.cuon();
    };

    BoVe.prototype.cuon = function () {
        var than = this.than;
        requestAnimationFrame(function () {
            than.scrollTop = than.scrollHeight;
        });
    };

    function ketNoi(duongDan, xuLy) {
        var ws = null, lanThu = 0, dong = false, hangCho = [];
        var giao = location.protocol === 'https:' ? 'wss://' : 'ws://';

        function mo() {
            ws = new WebSocket(giao + location.host + duongDan);
            ws.onopen = function () {
                lanThu = 0;
                if (xuLy.mo) xuLy.mo();
                while (hangCho.length) ws.send(hangCho.shift());
            };
            ws.onmessage = function (e) {
                var dl;
                try { dl = JSON.parse(e.data); } catch (loi) { return; }
                xuLy.tin(dl);
            };
            ws.onclose = function () {
                if (dong) return;
                if (xuLy.mat) xuLy.mat();
                lanThu++;
                setTimeout(mo, Math.min(15000, 800 * Math.pow(2, lanThu)));
            };
        }

        mo();
        return {
            gui: function (dl) {
                var chu = JSON.stringify(dl);
                if (ws && ws.readyState === 1) ws.send(chu);
                else if (hangCho.length < 20) hangCho.push(chu);
            },
            dong: function () {
                dong = true;
                if (ws) ws.close();
            }
        };
    }

    function tuGian(o) {
        o.style.height = 'auto';
        o.style.height = Math.min(o.scrollHeight, 120) + 'px';
    }

    window.BepNeChat = {tao: tao, gio: gio, chuDau: chuDau, BoVe: BoVe, ketNoi: ketNoi, tuGian: tuGian};
})();
