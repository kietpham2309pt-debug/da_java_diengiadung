(function () {
    'use strict';

    if (!window.BepNeChat) return;
    var C = window.BepNeChat;
    var soMenu = document.getElementById('qt-so-tin');
    var ungDung = document.getElementById('tn');
    var daBao = {};
    var kenh;

    function datSoMenu(n) {
        if (!soMenu) return;
        soMenu.hidden = n <= 0;
        soMenu.textContent = n > 99 ? '99+' : String(n);
    }

    function baoNoi(p) {
        var hop = document.querySelector('.tn-thong-bao');
        if (hop) hop.remove();
        hop = C.tao('div', 'tn-thong-bao');
        hop.setAttribute('role', 'status');
        hop.appendChild(C.tao('span', 'cb-anh cb-anh--nho', C.chuDau(p.ten)));
        var chu = C.tao('span', null, p.ten + ' cần tư vấn' + (p.tinCuoi ? ': "' + p.tinCuoi.slice(0, 60) + '"' : ''));
        chu.style.flex = '1';
        hop.appendChild(chu);
        var a = C.tao('a', null, 'Trả lời');
        a.href = '/admin/khach-hang/tin-nhan?phien=' + p.id;
        hop.appendChild(a);
        document.body.appendChild(hop);
        setTimeout(function () { hop.remove(); }, 8000);
    }

    var trang = ungDung ? taoTrangTinNhan() : null;

    kenh = C.ketNoi('/ws/chat-quan-tri', {
        mo: function () {
            if (trang) trang.moLai();
        },
        tin: function (dl) {
            if (dl.loai === 'TONG') {
                datSoMenu(dl.soChuaDoc);
            } else if (dl.loai === 'PHIEN') {
                if (trang) trang.capNhatPhien(dl.phien);
                var p = dl.phien;
                if (!trang && p.trangThai !== 'BOT' && p.soChuaDoc > 0 && daBao[p.id] !== p.soChuaDoc) {
                    daBao[p.id] = p.soChuaDoc;
                    baoNoi(p);
                }
            } else if (trang) {
                trang.nhan(dl);
            }
        }
    });

    function taoTrangTinNhan() {
        var dsThan = document.getElementById('tn-ds-than');
        var oTim = document.getElementById('tn-tim');
        var khungTrong = document.getElementById('tn-khung-trong');
        var khungChat = document.getElementById('tn-khung-chat');
        var than = document.getElementById('tn-than');
        var ten = document.getElementById('tn-ten');
        var phu = document.getElementById('tn-phu');
        var anh = document.getElementById('tn-anh');
        var cham = document.getElementById('tn-cham');
        var nutKetThuc = document.getElementById('tn-ket-thuc');
        var lienKetKhach = document.getElementById('tn-lien-ket-khach');
        var form = document.getElementById('tn-form');
        var oNhap = document.getElementById('tn-o-nhap');
        var nutGui = document.getElementById('tn-gui');
        var dangGo = document.getElementById('tn-dang-go');
        var nhanh = document.getElementById('tn-nhanh');
        var danhSach = {};
        var dangChon = ungDung.dataset.phien ? Number(ungDung.dataset.phien) : null;
        var boLoc = 'tat-ca';
        var henGo, lanGoCuoi = 0;

        var boVe = new C.BoVe(than, {
            laMinh: function (t) { return t.nguoiGui === 'NHAN_VIEN'; },
            anh: function (t) {
                return t.nguoiGui === 'BOT' ? {chu: 'B'} : {chu: C.chuDau(t.tenNguoiGui), lop: 'cb-anh--khach'};
            },
            lamLienKet: function (t) { return t.nguoiGui === 'BOT'; }
        });

        function nhanTrangThai(p) {
            if (p.trangThai === 'CHO_NHAN_VIEN') return {chu: 'Chờ tư vấn', lop: 'tn-nhan tn-nhan--cho'};
            if (p.trangThai === 'DANG_TU_VAN') return {chu: 'Đang tư vấn', lop: 'tn-nhan tn-nhan--tu-van'};
            return {chu: 'Trợ lý tự động', lop: 'tn-nhan'};
        }

        function gioNgan(iso) {
            var homNay = new Date().toISOString().substring(0, 10);
            return iso.substring(0, 10) === homNay ? C.gio(iso) : iso.substring(8, 10) + '/' + iso.substring(5, 7);
        }

        function veDanhSach() {
            var tuKhoa = (oTim.value || '').trim().toLowerCase();
            var ds = Object.keys(danhSach).map(function (k) { return danhSach[k]; })
                .filter(function (p) {
                    if (boLoc === 'can-tu-van' && p.trangThai === 'BOT') return false;
                    if (!tuKhoa) return true;
                    return (p.ten || '').toLowerCase().indexOf(tuKhoa) >= 0 || (p.soDienThoai || '').indexOf(tuKhoa) >= 0;
                })
                .sort(function (a, b) {
                    var uuTien = (b.soChuaDoc > 0) - (a.soChuaDoc > 0);
                    return uuTien || (a.capNhatLuc < b.capNhatLuc ? 1 : -1);
                });
            dsThan.textContent = '';
            if (!ds.length) {
                dsThan.appendChild(C.tao('div', 'tn-trong', boLoc === 'can-tu-van' ? 'Chưa có khách nào chờ tư vấn.' : 'Chưa có cuộc trò chuyện nào.'));
                return;
            }
            ds.forEach(function (p) {
                var nut = C.tao('button', 'tn-muc' + (p.id === dangChon ? ' dang-chon' : '') + (p.soChuaDoc > 0 ? ' tn-muc--chua-doc' : ''));
                nut.type = 'button';
                var oAnh = C.tao('span', 'tn-muc__anh');
                oAnh.appendChild(C.tao('span', 'cb-anh', C.chuDau(p.ten)));
                oAnh.appendChild(C.tao('span', 'cb-cham' + (p.dangTrucTuyen ? '' : ' cb-cham--tat')));
                nut.appendChild(oAnh);
                var chu = C.tao('span', 'tn-muc__chu');
                var hang1 = C.tao('span', 'tn-muc__hang');
                hang1.appendChild(C.tao('span', 'tn-muc__ten', p.ten));
                hang1.appendChild(C.tao('span', 'tn-muc__gio', gioNgan(p.capNhatLuc)));
                chu.appendChild(hang1);
                var hang2 = C.tao('span', 'tn-muc__hang');
                var tienTo = p.nguoiGuiCuoi === 'NHAN_VIEN' ? 'Bạn: ' : (p.nguoiGuiCuoi === 'BOT' ? 'Trợ lý: ' : '');
                hang2.appendChild(C.tao('span', 'tn-muc__tin', tienTo + (p.tinCuoi || '')));
                if (p.soChuaDoc > 0) hang2.appendChild(C.tao('span', 'tn-so', p.soChuaDoc > 9 ? '9+' : String(p.soChuaDoc)));
                chu.appendChild(hang2);
                var nhan = nhanTrangThai(p);
                var nhanE = C.tao('span', nhan.lop, nhan.chu);
                nhanE.style.marginTop = '5px';
                chu.appendChild(nhanE);
                nut.appendChild(chu);
                nut.addEventListener('click', function () { chon(p.id); });
                dsThan.appendChild(nut);
            });
        }

        function veDau(p) {
            ten.textContent = p.ten;
            anh.textContent = C.chuDau(p.ten);
            cham.className = 'cb-cham' + (p.dangTrucTuyen ? '' : ' cb-cham--tat');
            var nhan = nhanTrangThai(p);
            phu.textContent = '';
            var e = C.tao('span', nhan.lop, nhan.chu);
            phu.appendChild(e);
            phu.appendChild(document.createTextNode(' ' + (p.vangLai ? 'Khách vãng lai' : 'Thành viên #' + p.khachHangId)
                + (p.soDienThoai ? ' · ' + p.soDienThoai : '') + (p.dangTrucTuyen ? ' · Đang trực tuyến' : ' · Ngoại tuyến')));
            nutKetThuc.hidden = p.trangThai === 'BOT';
            lienKetKhach.hidden = p.vangLai;
            if (!p.vangLai) lienKetKhach.href = '/admin/khach-hang?tuKhoa=' + encodeURIComponent(p.soDienThoai || p.ten);
        }

        function chon(id) {
            dangChon = id;
            history.replaceState(null, '', '/admin/khach-hang/tin-nhan?phien=' + id);
            khungTrong.hidden = true;
            khungChat.hidden = false;
            dangGo.hidden = true;
            boVe.xoa();
            veDanhSach();
            fetch('/admin/khach-hang/tin-nhan/' + id + '/lich-su', {credentials: 'same-origin'})
                .then(function (r) { return r.json(); })
                .then(function (dl) {
                    if (dangChon !== id) return;
                    danhSach[id] = dl.phien;
                    veDau(dl.phien);
                    boVe.xoa();
                    dl.tinNhan.forEach(function (t) { boVe.them(t); });
                    kenh.gui({loai: 'MO', phienId: id});
                    oNhap.focus();
                });
        }

        function nhan(dl) {
            if (dl.loai === 'TIN') {
                if (dl.phienId === dangChon) {
                    if (dl.tin.nguoiGui === 'KHACH') dangGo.hidden = true;
                    boVe.them(dl.tin);
                    if (dl.tin.nguoiGui === 'KHACH' && document.visibilityState === 'visible') {
                        kenh.gui({loai: 'MO', phienId: dangChon});
                    }
                }
            } else if (dl.loai === 'DANG_GO' && dl.phienId === dangChon) {
                dangGo.hidden = false;
                boVe.cuon();
                clearTimeout(henGo);
                henGo = setTimeout(function () { dangGo.hidden = true; }, 3500);
            } else if (dl.loai === 'KHACH_DA_XEM' && dl.phienId === dangChon) {
                boVe.danhDauDaXem();
            } else if (dl.loai === 'LOI') {
                than.appendChild(C.tao('div', 'cb-he-thong', dl.thongBao));
                boVe.cuon();
            }
        }

        function capNhatPhien(p) {
            danhSach[p.id] = p;
            if (p.id === dangChon) veDau(p);
            veDanhSach();
        }

        function taiDanhSach() {
            return fetch('/admin/khach-hang/tin-nhan/danh-sach', {credentials: 'same-origin'})
                .then(function (r) { return r.json(); })
                .then(function (ds) {
                    danhSach = {};
                    ds.forEach(function (p) { danhSach[p.id] = p; });
                    veDanhSach();
                });
        }

        oTim.addEventListener('input', veDanhSach);
        document.querySelectorAll('[data-loc]').forEach(function (nut) {
            nut.addEventListener('click', function () {
                boLoc = nut.dataset.loc;
                document.querySelectorAll('[data-loc]').forEach(function (n) { n.classList.toggle('dang-chon', n === nut); });
                veDanhSach();
            });
        });
        nutKetThuc.addEventListener('click', function () {
            if (dangChon) kenh.gui({loai: 'KET_THUC', phienId: dangChon});
        });
        nhanh.querySelectorAll('.cb-chip').forEach(function (chip) {
            chip.addEventListener('click', function () {
                oNhap.value = chip.dataset.mau;
                C.tuGian(oNhap);
                nutGui.disabled = false;
                oNhap.focus();
            });
        });
        oNhap.addEventListener('input', function () {
            C.tuGian(oNhap);
            nutGui.disabled = !oNhap.value.trim();
            var bay = Date.now();
            if (dangChon && bay - lanGoCuoi > 2000) {
                lanGoCuoi = bay;
                kenh.gui({loai: 'DANG_GO', phienId: dangChon});
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
            if (!noiDung || !dangChon) return;
            kenh.gui({loai: 'GUI', phienId: dangChon, noiDung: noiDung});
            oNhap.value = '';
            C.tuGian(oNhap);
            nutGui.disabled = true;
        });
        document.addEventListener('visibilitychange', function () {
            if (document.visibilityState === 'visible' && dangChon && danhSach[dangChon] && danhSach[dangChon].soChuaDoc > 0) {
                kenh.gui({loai: 'MO', phienId: dangChon});
            }
        });

        taiDanhSach().then(function () {
            if (dangChon) chon(dangChon);
        });

        return {
            nhan: nhan,
            capNhatPhien: capNhatPhien,
            moLai: function () {
                taiDanhSach();
            }
        };
    }
})();
