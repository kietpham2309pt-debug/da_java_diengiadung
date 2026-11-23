(function () {
    'use strict';

    var goc = document.getElementById('ho-tro');
    if (!goc || !window.BepNeChat) return;
    var C = window.BepNeChat;

    var bong = document.getElementById('ho-tro-bong');
    var khung = document.getElementById('ho-tro-khung');
    var soBadge = document.getElementById('ho-tro-so');
    var than = document.getElementById('ho-tro-than');
    var goiY = document.getElementById('ho-tro-goi-y');
    var form = document.getElementById('ho-tro-form');
    var oNhap = document.getElementById('ho-tro-o-nhap');
    var nutGui = document.getElementById('ho-tro-gui');
    var dangGo = document.getElementById('ho-tro-dang-go');
    var dangGoChu = document.getElementById('ho-tro-dang-go-chu');
    var phu = document.getElementById('ho-tro-phu');
    var anh = document.getElementById('ho-tro-anh');
    var ketNoiLai = document.getElementById('ho-tro-ket-noi');
    var KHOA = 'bepne-chat-da-mo';

    var boVe = new C.BoVe(than, {
        laMinh: function (t) { return t.nguoiGui === 'KHACH'; },
        anh: function (t) {
            return t.nguoiGui === 'NHAN_VIEN' ? {chu: C.chuDau(t.tenNguoiGui), lop: 'cb-anh--nv'} : {chu: 'B'};
        },
        lamLienKet: function (t) { return t.nguoiGui === 'BOT'; }
    });

    var kenh = null, daTai = false, dangTai = false, soChuaDoc = 0, phien = null;
    var tinCuoiBot = null, hangBot = [], dangChoBot = false, henGo = null, lanGoCuoi = 0;

    function luu(k, v) { try { localStorage.setItem(k, v); } catch (e) { } }
    function doc(k) { try { return localStorage.getItem(k); } catch (e) { return null; } }

    function datSo(n) {
        soChuaDoc = n;
        soBadge.hidden = n <= 0;
        soBadge.textContent = n > 9 ? '9+' : String(n);
    }

    function dangXem() {
        return !khung.hidden && document.visibilityState === 'visible';
    }

    function capNhatPhien(p) {
        phien = p;
        if (p.trangThai === 'DANG_TU_VAN') {
            phu.textContent = 'Nhân viên ' + (p.tenNhanVien || '') + ' đang hỗ trợ bạn';
            anh.textContent = C.chuDau(p.tenNhanVien);
            anh.classList.add('cb-anh--nv');
        } else if (p.trangThai === 'CHO_NHAN_VIEN') {
            phu.textContent = 'Đang kết nối nhân viên tư vấn...';
            anh.textContent = 'B';
            anh.classList.remove('cb-anh--nv');
        } else {
            phu.textContent = 'Trợ lý tự động · Trả lời ngay';
            anh.textContent = 'B';
            anh.classList.remove('cb-anh--nv');
        }
        veGoiY();
    }

    function veGoiY() {
        goiY.textContent = '';
        if (!tinCuoiBot || !tinCuoiBot.goiY || !tinCuoiBot.goiY.length) return;
        if (phien && phien.trangThai !== 'BOT') return;
        tinCuoiBot.goiY.forEach(function (g) {
            var nut = C.tao('button', 'cb-chip' + (g.ma === 'nhan-vien' ? ' cb-chip--chinh' : ''), g.nhan);
            nut.type = 'button';
            nut.addEventListener('click', function () {
                goiY.textContent = '';
                kenh.gui({loai: 'CHON', ma: g.ma});
            });
            goiY.appendChild(nut);
        });
    }

    function veFormThongTin() {
        var f = C.tao('form', 'cb-form-tt');
        f.noValidate = true;
        var ten = C.tao('input');
        ten.placeholder = 'Họ và tên';
        ten.maxLength = 80;
        ten.autocomplete = 'name';
        var sdt = C.tao('input');
        sdt.placeholder = 'Số điện thoại';
        sdt.inputMode = 'tel';
        sdt.maxLength = 12;
        sdt.autocomplete = 'tel';
        var loi = C.tao('div', 'cb-form-tt__loi');
        var hang = C.tao('div', 'cb-form-tt__nut');
        var boQua = C.tao('button', 'cb-chip', 'Bỏ qua');
        boQua.type = 'button';
        var gui = C.tao('button', 'cb-chip cb-chip--chinh', 'Gửi thông tin');
        gui.type = 'submit';
        hang.appendChild(boQua);
        hang.appendChild(gui);
        [ten, sdt, loi, hang].forEach(function (e) { f.appendChild(e); });
        f.addEventListener('submit', function (e) {
            e.preventDefault();
            var t = ten.value.trim(), s = sdt.value.replace(/[\s.]/g, '');
            if (t.length < 2) { loi.textContent = 'Vui lòng nhập họ tên (ít nhất 2 ký tự)'; ten.focus(); return; }
            if (!/^0\d{9}$/.test(s)) { loi.textContent = 'Số điện thoại gồm 10 chữ số, bắt đầu bằng 0'; sdt.focus(); return; }
            kenh.gui({loai: 'THONG_TIN', ten: t, soDienThoai: s});
            f.remove();
        });
        boQua.addEventListener('click', function () {
            kenh.gui({loai: 'THONG_TIN', ten: '', soDienThoai: ''});
            f.remove();
        });
        than.appendChild(f);
        boVe.cuon();
        setTimeout(function () { ten.focus(); }, 50);
    }

    function nhanTin(tin, laLichSu) {
        if (tin.nguoiGui === 'BOT' && !laLichSu) {
            hangBot.push(tin);
            if (!dangChoBot) xuLyHangBot();
            return;
        }
        veTin(tin, laLichSu);
    }

    function xuLyHangBot() {
        if (!hangBot.length) { dangChoBot = false; return; }
        dangChoBot = true;
        hienDangGo('Trợ lý đang trả lời');
        setTimeout(function () {
            anDangGo();
            veTin(hangBot.shift(), false);
            xuLyHangBot();
        }, 650);
    }

    function veTin(tin, laLichSu) {
        var cu = than.querySelector('.cb-form-tt');
        if (cu && !laLichSu) cu.remove();
        boVe.them(tin);
        if (tin.nguoiGui === 'BOT') {
            tinCuoiBot = tin;
            veGoiY();
            if (tin.canThongTin && !laLichSu) veFormThongTin();
        }
        if (!laLichSu && tin.nguoiGui !== 'KHACH') {
            if (dangXem()) kenh.gui({loai: 'DA_XEM'});
            else if (tin.nguoiGui === 'NHAN_VIEN' || tin.nguoiGui === 'HE_THONG') datSo(soChuaDoc + 1);
        }
    }

    function hienDangGo(chu) {
        dangGoChu.textContent = chu;
        dangGo.hidden = false;
        boVe.cuon();
    }

    function anDangGo() {
        dangGo.hidden = true;
    }

    function ketNoi() {
        if (kenh) return;
        kenh = C.ketNoi('/ws/chat', {
            mo: function () { ketNoiLai.hidden = true; },
            mat: function () { ketNoiLai.hidden = false; },
            tin: function (dl) {
                if (dl.loai === 'TIN') {
                    if (dl.tin.nguoiGui === 'NHAN_VIEN') anDangGo();
                    nhanTin(dl.tin, false);
                } else if (dl.loai === 'PHIEN') {
                    capNhatPhien(dl.phien);
                } else if (dl.loai === 'DANG_GO') {
                    hienDangGo((dl.ten || 'Nhân viên') + ' đang soạn tin');
                    clearTimeout(henGo);
                    henGo = setTimeout(anDangGo, 3500);
                } else if (dl.loai === 'DA_XEM') {
                    boVe.danhDauDaXem();
                } else if (dl.loai === 'LOI') {
                    var loi = C.tao('div', 'cb-he-thong', dl.thongBao);
                    than.appendChild(loi);
                    boVe.cuon();
                }
            }
        });
    }

    function taiPhien(tao) {
        if (dangTai) return Promise.resolve();
        dangTai = true;
        return fetch('/chat/phien?tao=' + tao, {headers: {'Accept': 'application/json'}, credentials: 'same-origin'})
            .then(function (r) { return r.ok ? r.json() : null; })
            .then(function (dl) {
                dangTai = false;
                if (!dl || !dl.coPhien) return;
                daTai = true;
                luu(KHOA, '1');
                boVe.xoa();
                dl.tinNhan.forEach(function (t) { nhanTin(t, true); });
                capNhatPhien(dl);
                var cuoi = dl.tinNhan[dl.tinNhan.length - 1];
                if (cuoi && cuoi.canThongTin && dl.trangThai === 'BOT') veFormThongTin();
                datSo(dangXem() ? 0 : dl.soChuaDoc);
                ketNoi();
                if (dangXem() && dl.soChuaDoc > 0) kenh.gui({loai: 'DA_XEM'});
            })
            .catch(function () { dangTai = false; });
    }

    function mo() {
        khung.hidden = false;
        goc.classList.add('dang-mo');
        goc.classList.remove('chao');
        bong.setAttribute('aria-expanded', 'true');
        if (!daTai) {
            taiPhien(true);
        } else if (soChuaDoc > 0) {
            kenh.gui({loai: 'DA_XEM'});
        }
        datSo(0);
        boVe.cuon();
        setTimeout(function () { oNhap.focus(); }, 60);
    }

    function dong() {
        khung.hidden = true;
        goc.classList.remove('dang-mo');
        bong.setAttribute('aria-expanded', 'false');
        bong.focus();
    }

    bong.addEventListener('click', function () {
        if (khung.hidden) mo(); else dong();
    });
    document.getElementById('ho-tro-thu').addEventListener('click', dong);
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape' && !khung.hidden) dong();
    });

    oNhap.addEventListener('input', function () {
        C.tuGian(oNhap);
        nutGui.disabled = !oNhap.value.trim();
        var bay = Date.now();
        if (kenh && phien && phien.trangThai !== 'BOT' && bay - lanGoCuoi > 2000) {
            lanGoCuoi = bay;
            kenh.gui({loai: 'DANG_GO'});
        }
    });
    oNhap.addEventListener('keydown', function (e) {
        if (e.key === 'Enter' && !e.shiftKey && !e.isComposing) {
            e.preventDefault();
            form.requestSubmit();
        }
    });
    form.addEventListener('submit', function (e) {
        e.preventDefault();
        var noiDung = oNhap.value.trim();
        if (!noiDung || !kenh) return;
        kenh.gui({loai: 'GUI', noiDung: noiDung});
        oNhap.value = '';
        C.tuGian(oNhap);
        nutGui.disabled = true;
    });

    document.addEventListener('visibilitychange', function () {
        if (dangXem() && soChuaDoc > 0 && kenh) {
            kenh.gui({loai: 'DA_XEM'});
            datSo(0);
        }
    });

    if (doc(KHOA) === '1') {
        taiPhien(false);
    }

    try {
        if (!sessionStorage.getItem('bepne-chat-chao')) {
            setTimeout(function () {
                if (!khung.hidden) return;
                goc.classList.add('chao');
                sessionStorage.setItem('bepne-chat-chao', '1');
                setTimeout(function () { goc.classList.remove('chao'); }, 6000);
            }, 3500);
        }
    } catch (e) { }
})();
