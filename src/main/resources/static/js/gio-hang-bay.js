(function () {
  'use strict';

  var form = document.querySelector('form.ct__dat');
  if (!form || !window.fetch || !window.FormData || !Element.prototype.animate) return;

  var nut = form.querySelector('.ct__them');
  var anh = document.getElementById('anh-to');
  var giamChuyenDong = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  var henAnThongBao = null;

  function layGio() {
    var ds = document.querySelectorAll('a.nut-tron[href*="/gio-hang"]');
    for (var i = 0; i < ds.length; i++) {
      var k = ds[i].getBoundingClientRect();
      if (k.width > 0 && k.height > 0) return ds[i];
    }
    return ds[0] || null;
  }

  function cho(hoatAnh) {
    return hoatAnh.finished ? hoatAnh.finished : new Promise(function (xong) { hoatAnh.onfinish = xong; });
  }

  function taoPhan(lop, cha) {
    var el = document.createElement('div');
    el.className = lop;
    cha.appendChild(el);
    return el;
  }

  function dongGoiVaQuang(khungAnh, duongDanAnh, gio) {
    var rongHop = Math.max(90, Math.min(170, Math.min(khungAnh.width, khungAnh.height) * 0.36));
    var caoHop = rongHop * 0.78;
    var hopX = khungAnh.left + khungAnh.width / 2 - rongHop / 2;
    var hopY = khungAnh.top + khungAnh.height * 0.62 - caoHop / 2;

    var hop = document.createElement('div');
    hop.className = 'bay-hop';
    hop.style.left = hopX + 'px';
    hop.style.top = hopY + 'px';
    hop.style.width = rongHop + 'px';
    hop.style.height = caoHop + 'px';
    document.body.appendChild(hop);

    var bongDo = taoPhan('bay-hop__bong', hop);
    var thanSau = taoPhan('bay-hop__sau', hop);
    var anhBay = document.createElement('img');
    anhBay.className = 'bay-hop__anh';
    anhBay.alt = '';
    anhBay.src = duongDanAnh;
    anhBay.style.left = (khungAnh.left - hopX) + 'px';
    anhBay.style.top = (khungAnh.top - hopY) + 'px';
    anhBay.style.width = khungAnh.width + 'px';
    anhBay.style.height = khungAnh.height + 'px';
    hop.appendChild(anhBay);
    var thanTruoc = taoPhan('bay-hop__truoc', hop);
    var chu = document.createElement('span');
    chu.className = 'bay-hop__chu';
    chu.textContent = 'Bep Nè';
    thanTruoc.appendChild(chu);
    var napTrai = taoPhan('bay-hop__nap bay-hop__nap--trai', hop);
    var napPhai = taoPhan('bay-hop__nap bay-hop__nap--phai', hop);
    var bangKeo = taoPhan('bay-hop__bang', hop);

    var hienRa = [
      { opacity: 0, transform: 'translateY(18px) scale(.86)' },
      { opacity: 1, transform: 'translateY(0) scale(1)' }
    ];
    var hien = { duration: 260, easing: 'cubic-bezier(.2,.8,.3,1)', fill: 'both' };
    thanSau.animate(hienRa, hien);
    thanTruoc.animate(hienRa, hien);
    bongDo.animate([{ opacity: 0 }, { opacity: 1 }], hien);
    napTrai.animate([{ opacity: 0, transform: 'rotate(-150deg)' }, { opacity: 1, transform: 'rotate(-150deg)' }], hien);
    napPhai.animate([{ opacity: 0, transform: 'rotate(150deg)' }, { opacity: 1, transform: 'rotate(150deg)' }], hien);

    var tamAnhX = khungAnh.left + khungAnh.width / 2;
    var tamAnhY = khungAnh.top + khungAnh.height / 2;
    var tyLe = (rongHop * 0.86) / Math.max(khungAnh.width, khungAnh.height * 1.2);
    var dichX = hopX + rongHop / 2 - tamAnhX;
    var treoY = hopY - caoHop * 0.55 - tamAnhY;
    var roiY = hopY + caoHop * 0.55 - tamAnhY;

    var thuNho = anhBay.animate([
      { transform: 'translate(0,0) scale(1)', borderRadius: '0px' },
      { transform: 'translate(' + dichX + 'px,' + treoY + 'px) scale(' + tyLe + ') rotate(-4deg)', borderRadius: '10px', offset: 0.62 },
      { transform: 'translate(' + dichX + 'px,' + roiY + 'px) scale(' + (tyLe * 0.9) + ') rotate(2deg)', borderRadius: '10px' }
    ], { duration: 620, delay: 80, easing: 'cubic-bezier(.45,0,.3,1)', fill: 'forwards' });

    return cho(thuNho).then(function () {
      anhBay.style.visibility = 'hidden';
      var nayHop = hop.animate([
        { transform: 'translateY(0) scaleY(1)' },
        { transform: 'translateY(3px) scaleY(.94)' },
        { transform: 'translateY(0) scaleY(1)' }
      ], { duration: 180, easing: 'ease-out' });
      var gapTrai = napTrai.animate([{ transform: 'rotate(-150deg)' }, { transform: 'rotate(0deg)' }],
        { duration: 240, easing: 'cubic-bezier(.5,0,.4,1.25)', fill: 'forwards' });
      var gapPhai = napPhai.animate([{ transform: 'rotate(150deg)' }, { transform: 'rotate(0deg)' }],
        { duration: 240, delay: 110, easing: 'cubic-bezier(.5,0,.4,1.25)', fill: 'forwards' });
      var danBang = bangKeo.animate([{ transform: 'scaleX(0)' }, { transform: 'scaleX(1)' }],
        { duration: 200, delay: 330, easing: 'ease-out', fill: 'forwards' });
      return Promise.all([cho(nayHop), cho(gapTrai), cho(gapPhai), cho(danBang)]);
    }).then(function () {
      var khungGio = gio.getBoundingClientRect();
      var dichToiX = khungGio.left + khungGio.width / 2 - (hopX + rongHop / 2);
      var dichToiY = khungGio.top + khungGio.height / 2 - (hopY + caoHop / 2);
      var dinhCung = Math.min(0, dichToiY) - Math.max(120, Math.abs(dichToiX) * 0.28);
      var tyLeCuoi = 26 / rongHop;
      var soKhung = 22;
      var khung = [];
      for (var i = 0; i <= soKhung; i++) {
        var t = i / soKhung;
        var x = 2 * (1 - t) * t * (dichToiX * 0.45) + t * t * dichToiX;
        var y = 2 * (1 - t) * t * dinhCung + t * t * dichToiY;
        var tyLe = 1 - (1 - tyLeCuoi) * Math.pow(t, 1.15);
        var xoay = -10 + 380 * Math.pow(t, 1.3);
        khung.push({
          transform: 'translate(' + x + 'px,' + y + 'px) rotate(' + xoay + 'deg) scale(' + tyLe + ')',
          opacity: t > 0.92 ? (1 - t) / 0.08 : 1,
          offset: t
        });
      }
      bongDo.animate([{ opacity: 1 }, { opacity: 0 }], { duration: 160, fill: 'forwards' });
      khung[0].transform = 'translate(0,-10px) rotate(-10deg) scale(1.04)';
      var nhac = hop.animate([
        { transform: 'translate(0,0) rotate(0deg) scale(1)' },
        { transform: 'translate(0,-10px) rotate(-10deg) scale(1.04)' }
      ], { duration: 140, easing: 'ease-out', fill: 'forwards' });
      return cho(nhac).then(function () {
        var bay = hop.animate(khung, { duration: 760, easing: 'cubic-bezier(.35,.05,.6,1)', fill: 'forwards' });
        return cho(bay);
      });
    }).then(function () {
      hop.remove();
    }, function () {
      hop.remove();
    });
  }

  function nayGio(gio) {
    if (!gio || giamChuyenDong) return;
    gio.animate([
      { transform: 'translateY(0) rotate(0) scale(1)' },
      { transform: 'translateY(4px) rotate(-12deg) scale(1.18)' },
      { transform: 'translateY(-3px) rotate(9deg) scale(1.08)' },
      { transform: 'translateY(1px) rotate(-4deg) scale(1.02)' },
      { transform: 'translateY(0) rotate(0) scale(1)' }
    ], { duration: 560, easing: 'ease-out' });
  }

  function doiSo(soMoi) {
    var oSo = document.querySelectorAll('a.nut-tron[href*="/gio-hang"] .nut-tron__so');
    for (var i = 0; i < oSo.length; i++) {
      (function (o) {
        var soCu = parseInt(o.textContent, 10) || 0;
        if (giamChuyenDong || soMoi <= soCu) {
          o.textContent = soMoi;
          return;
        }
        var buoc = Math.min(soMoi - soCu, 8);
        var dem = 0;
        var nhip = setInterval(function () {
          dem++;
          o.textContent = dem >= buoc ? soMoi : soCu + Math.round((soMoi - soCu) * dem / buoc);
          if (dem >= buoc) clearInterval(nhip);
        }, 55);
        o.animate([
          { transform: 'scale(1)', boxShadow: '0 0 0 0 rgba(203,170,125,.7)' },
          { transform: 'scale(1.75)', boxShadow: '0 0 0 6px rgba(203,170,125,.35)', offset: 0.35 },
          { transform: 'scale(.9)', boxShadow: '0 0 0 10px rgba(203,170,125,0)', offset: 0.7 },
          { transform: 'scale(1)', boxShadow: '0 0 0 0 rgba(203,170,125,0)' }
        ], { duration: 620, easing: 'ease-out' });
      })(oSo[i]);
    }
  }

  function thongBaoNho(noiDung, laLoi) {
    var tb = document.querySelector('.bay-tb');
    if (!tb) {
      tb = document.createElement('div');
      tb.className = 'bay-tb';
      tb.setAttribute('role', 'status');
      document.body.appendChild(tb);
    }
    tb.className = 'bay-tb' + (laLoi ? ' bay-tb--loi' : '');
    tb.innerHTML = '';
    var chu = document.createElement('span');
    chu.textContent = noiDung;
    tb.appendChild(chu);
    if (!laLoi) {
      var lienKet = document.createElement('a');
      lienKet.href = (layGio() || {}).href || '/gio-hang';
      lienKet.textContent = 'Xem giỏ hàng';
      tb.appendChild(lienKet);
    }
    tb.classList.add('hien');
    clearTimeout(henAnThongBao);
    henAnThongBao = setTimeout(function () { tb.classList.remove('hien'); }, 3800);
  }

  form.addEventListener('submit', function (su) {
    su.preventDefault();
    if (nut.disabled) return;
    nut.disabled = true;
    nut.classList.add('dang-them');
    var khungAnh = anh ? anh.getBoundingClientRect() : null;
    var duongDan = anh ? (anh.currentSrc || anh.src) : '';
    var gio = layGio();

    fetch(form.action, {
      method: 'POST',
      body: new URLSearchParams(new FormData(form)),
      credentials: 'same-origin',
      headers: { 'X-Requested-With': 'XMLHttpRequest', 'Accept': 'application/json' }
    }).then(function (phanHoi) {
      return phanHoi.json().then(function (duLieu) { return { ok: phanHoi.ok, duLieu: duLieu }; });
    }).then(function (kq) {
      var xong = function () {
        nut.disabled = false;
        nut.classList.remove('dang-them');
      };
      if (!kq.ok || !kq.duLieu.ok) {
        thongBaoNho(kq.duLieu.loi || 'Chưa thêm được sản phẩm vào giỏ hàng', true);
        xong();
        return;
      }
      var hoanTat = function () {
        nayGio(gio);
        doiSo(kq.duLieu.soMon);
        thongBaoNho(kq.duLieu.thongBao, false);
        xong();
      };
      if (giamChuyenDong || !khungAnh || !gio || khungAnh.width === 0) {
        hoanTat();
        return;
      }
      dongGoiVaQuang(khungAnh, duongDan, gio).then(hoanTat);
    }).catch(function () {
      form.submit();
    });
  });
})();
