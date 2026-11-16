(function () {
  var LY_DO_KHAC = 'Lý do khác';

  var oCanKiem = function (form) {
    var daCo = {};
    return Array.prototype.filter.call(form.querySelectorAll('input, select, textarea'), function (o) {
      if (!o.name || o.name.charAt(0) === '_' || o.type === 'hidden' || o.type === 'submit' || o.type === 'button') {
        return false;
      }
      if (o.type === 'radio') {
        if (daCo[o.name]) {
          return false;
        }
        daCo[o.name] = true;
      }
      return !o.closest('[hidden]');
    });
  };

  var lamSachNhan = function (chu) {
    return chu.replace(/\*/g, '').replace(/\((không bắt buộc|nếu có|bắt buộc)\)/g, '').replace(/\s+/g, ' ').trim();
  };

  var nhanCua = function (o) {
    if (o.dataset.nhan) {
      return o.dataset.nhan;
    }
    if (o.id) {
      var nhan = document.querySelector('label[for="' + o.id + '"]');
      if (nhan) {
        return lamSachNhan(nhan.textContent);
      }
    }
    var nhom = o.closest('fieldset');
    var chuNhom = nhom && nhom.querySelector('legend');
    return chuNhom ? lamSachNhan(chuNhom.textContent) : o.name;
  };

  var viTriLoi = function (o) {
    if (o.type === 'radio') {
      return o.closest('fieldset') || o.parentNode;
    }
    return o.closest('.o-nhap, .tt-dong-y, .o-tich') || o.parentNode;
  };

  var kiemTraO = function (form, o) {
    var nhan = nhanCua(o);
    var dau = nhan.charAt(0).toLowerCase() + nhan.slice(1);
    if (o.type === 'radio') {
      var coChon = form.querySelector('input[type="radio"][name="' + o.name + '"]:checked');
      if (o.required && !coChon) {
        return { thieu: true, thongBao: o.dataset.thieu || 'Vui lòng chọn ' + dau };
      }
      return null;
    }
    if (o.type === 'checkbox') {
      return o.required && !o.checked ? { thieu: true, thongBao: o.dataset.thieu || 'Vui lòng tích chọn ' + dau } : null;
    }
    var giaTri = (o.value || '').trim();
    if (!giaTri) {
      if (o.required) {
        return { thieu: true, thongBao: o.dataset.thieu || (o.tagName === 'SELECT' ? 'Vui lòng chọn ' : 'Vui lòng nhập ') + dau };
      }
      return null;
    }
    var sai = function (macDinh) {
      return { thieu: false, thongBao: o.dataset.sai || macDinh };
    };
    if (o.pattern && !new RegExp('^(?:' + o.pattern + ')$').test(giaTri)) {
      return sai(nhan + ' chưa đúng định dạng');
    }
    var ngan = parseInt(o.getAttribute('minlength'), 10);
    if (ngan && giaTri.length < ngan) {
      return sai(nhan + ' phải có ít nhất ' + ngan + ' ký tự');
    }
    if (o.type === 'email' && !/^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(giaTri)) {
      return sai('Email chưa đúng định dạng, ví dụ: ten@gmail.com');
    }
    if (o.dataset.giong && form.elements[o.dataset.giong] && form.elements[o.dataset.giong].value !== o.value) {
      return sai(nhan + ' chưa khớp');
    }
    return null;
  };

  var cacO = function (form, ten) {
    return form.querySelectorAll('[name="' + ten + '"]');
  };

  var danhDau = function (form, o, loi) {
    var noi = viTriLoi(o);
    Array.prototype.forEach.call(noi.children, function (con) {
      if (con.classList.contains('loi-nhap')) {
        con.remove();
      }
    });
    cacO(form, o.name).forEach(function (x) {
      x.setAttribute('aria-invalid', loi ? 'true' : 'false');
    });
    if (o.type === 'radio') {
      var nhomChon = noi.querySelector('.tt-pt');
      if (nhomChon) {
        nhomChon.classList.toggle('tt-pt--loi', !!loi);
      }
    }
    if (loi) {
      var dong = document.createElement('span');
      dong.className = 'loi-nhap';
      dong.textContent = loi.thongBao;
      noi.appendChild(dong);
    }
  };

  var taoDong = function (o, nhan, thongBao) {
    var li = document.createElement('li');
    li.dataset.truong = o ? o.name : '';
    var a = document.createElement('a');
    a.href = '#' + (o && o.id ? o.id : '');
    if (nhan) {
      var b = document.createElement('b');
      b.textContent = nhan;
      a.appendChild(b);
      a.appendChild(document.createTextNode(': ' + thongBao));
    } else {
      a.textContent = thongBao;
    }
    li.appendChild(a);
    return li;
  };

  var dungTomTat = function (form, dsLoi) {
    var cu = form.querySelector('[data-tom-tat-loi]');
    if (cu) {
      cu.remove();
    }
    var hop = document.createElement('div');
    hop.className = 'tom-tat-loi';
    hop.setAttribute('role', 'alert');
    hop.setAttribute('tabindex', '-1');
    hop.setAttribute('data-tom-tat-loi', '');
    var tieuDe = document.createElement('p');
    tieuDe.className = 'tom-tat-loi__tieu-de';
    tieuDe.textContent = 'Có ' + dsLoi.length + ' thông tin cần sửa trước khi tiếp tục';
    hop.appendChild(tieuDe);
    [['Còn thiếu', true], ['Chưa đúng', false]].forEach(function (nhom) {
      var ds = dsLoi.filter(function (l) { return l.loi.thieu === nhom[1]; });
      if (!ds.length) {
        return;
      }
      var khoi = document.createElement('div');
      khoi.className = 'tom-tat-loi__nhom' + (nhom[1] ? '' : ' tom-tat-loi__nhom--sai');
      var nhan = document.createElement('p');
      nhan.className = 'tom-tat-loi__nhan';
      nhan.textContent = nhom[0] + ' (' + ds.length + ')';
      var ul = document.createElement('ul');
      ds.forEach(function (l) {
        ul.appendChild(taoDong(l.o, nhanCua(l.o), l.loi.thongBao));
      });
      khoi.appendChild(nhan);
      khoi.appendChild(ul);
      hop.appendChild(khoi);
    });
    form.insertBefore(hop, form.firstElementChild);
    return hop;
  };

  var capNhatTomTat = function (form) {
    var hop = form.querySelector('[data-tom-tat-loi]');
    if (!hop) {
      return;
    }
    hop.querySelectorAll('.tom-tat-loi__nhom').forEach(function (khoi) {
      var so = khoi.querySelectorAll('li').length;
      var nhan = khoi.querySelector('.tom-tat-loi__nhan');
      if (!so) {
        khoi.remove();
      } else if (nhan) {
        nhan.textContent = nhan.textContent.replace(/\(\d+\)/, '(' + so + ')');
      }
    });
    var conLai = hop.querySelectorAll('li').length;
    if (!conLai) {
      hop.remove();
      return;
    }
    var tieuDe = hop.querySelector('.tom-tat-loi__tieu-de span') || hop.querySelector('.tom-tat-loi__tieu-de');
    tieuDe.textContent = 'Có ' + conLai + ' thông tin cần sửa trước khi tiếp tục';
  };

  var denO = function (o) {
    if (!o) {
      return;
    }
    o.scrollIntoView({ block: 'center', behavior: 'smooth' });
    setTimeout(function () { o.focus({ preventScroll: true }); }, 250);
  };

  document.addEventListener('click', function (su) {
    var lienKet = su.target.closest('[data-tom-tat-loi] a');
    if (!lienKet) {
      return;
    }
    var form = lienKet.closest('form');
    var li = lienKet.closest('li');
    var o = form && li && li.dataset.truong ? cacO(form, li.dataset.truong)[0] : null;
    if (o) {
      su.preventDefault();
      denO(o);
    }
  });

  document.addEventListener('submit', function (su) {
    var form = su.target;
    if (!form.hasAttribute('data-kiem-tra') || (su.submitter && su.submitter.hasAttribute('formnovalidate'))) {
      return;
    }
    var dsLoi = [];
    oCanKiem(form).forEach(function (o) {
      var loi = kiemTraO(form, o);
      danhDau(form, o, loi);
      if (loi) {
        dsLoi.push({ o: o, loi: loi });
      }
    });
    if (!dsLoi.length) {
      var cu = form.querySelector('[data-tom-tat-loi]');
      if (cu) {
        cu.remove();
      }
      return;
    }
    su.preventDefault();
    su.stopImmediatePropagation();
    var hop = dungTomTat(form, dsLoi);
    if (form.closest('dialog')) {
      denO(dsLoi[0].o);
    } else {
      hop.scrollIntoView({ block: 'start', behavior: 'smooth' });
      hop.focus({ preventScroll: true });
    }
  }, true);

  var khiSua = function (su) {
    var o = su.target;
    var form = o.form;
    if (!form || !o.name || o.getAttribute('aria-invalid') !== 'true') {
      return;
    }
    if (form.hasAttribute('data-kiem-tra') && kiemTraO(form, o)) {
      return;
    }
    danhDau(form, o, null);
    var hop = form.querySelector('[data-tom-tat-loi]');
    if (hop) {
      hop.querySelectorAll('li[data-truong="' + o.name + '"]').forEach(function (li) { li.remove(); });
      capNhatTomTat(form);
    }
  };
  document.addEventListener('input', khiSua);
  document.addEventListener('change', khiSua);

  document.querySelectorAll('[data-tom-tat-loi]').forEach(function (hop) {
    var form = hop.closest('form');
    hop.querySelectorAll('li[data-truong]').forEach(function (li) {
      var o = form && li.dataset.truong ? cacO(form, li.dataset.truong)[0] : null;
      if (!o) {
        return;
      }
      var a = li.querySelector('a');
      var thongBao = a.textContent.trim();
      var moi = taoDong(o, nhanCua(o), thongBao);
      li.replaceWith(moi);
      cacO(form, o.name).forEach(function (x) { x.setAttribute('aria-invalid', 'true'); });
    });
    hop.scrollIntoView({ block: 'start' });
    hop.focus({ preventScroll: true });
  });

  document.addEventListener('click', function (su) {
    var mo = su.target.closest('[data-mo-hop]');
    if (mo) {
      var hop = document.getElementById(mo.dataset.moHop);
      if (hop && typeof hop.showModal === 'function') {
        hop.showModal();
      }
      return;
    }
    var dong = su.target.closest('[data-dong-hop]');
    if (dong) {
      dong.closest('dialog').close();
      return;
    }
    if (su.target.tagName === 'DIALOG' && su.target.classList.contains('hop')) {
      var khung = su.target.getBoundingClientRect();
      if (su.clientX < khung.left || su.clientX > khung.right || su.clientY < khung.top || su.clientY > khung.bottom) {
        su.target.close();
      }
    }
  });

  document.addEventListener('change', function (su) {
    var o = su.target;
    if (o.name !== 'lyDo' || !o.closest('.hop__form')) {
      return;
    }
    var form = o.form;
    var khoi = form.querySelector('[data-khi-khac]');
    var oGhi = form.querySelector('textarea[name="lyDoKhac"]');
    var laKhac = o.value === LY_DO_KHAC;
    khoi.hidden = !laKhac;
    oGhi.required = laKhac;
    if (laKhac) {
      oGhi.focus();
    } else {
      danhDau(form, oGhi, null);
      var tomTat = form.querySelector('[data-tom-tat-loi]');
      if (tomTat) {
        tomTat.querySelectorAll('li[data-truong="lyDoKhac"]').forEach(function (li) { li.remove(); });
        capNhatTomTat(form);
      }
    }
  });
})();
