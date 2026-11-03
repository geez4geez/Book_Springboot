package id.ac.ub.filkom.contoh.control;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.TransaksiPinjam;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.repository.TransaksiPinjamRepository;


@Service
public class PinjamService {

    private final AnggotaRepository anggotaRepository;
    private final BukuRepository bukuRepository;
    private final TransaksiPinjamRepository transaksiRepository;

    public PinjamService(AnggotaRepository anggotaRepository,
                         BukuRepository bukuRepository,
                         TransaksiPinjamRepository transaksiRepository) {
        this.anggotaRepository = anggotaRepository;
        this.bukuRepository = bukuRepository;
        this.transaksiRepository = transaksiRepository;
    }

    public List<Buku> daftarBuku() {
        return bukuRepository.findAll();
    }

    @Transactional
    public Bukti pinjamBuku(String idAnggota, String idBuku) {
        Anggota anggota = anggotaRepository.findById(idAnggota)
                .orElseThrow(() -> PinjamGagalException.tidakDitemukan("Anggota"));
        Buku buku = bukuRepository.findById(idBuku)
                .orElseThrow(() -> PinjamGagalException.tidakDitemukan("Buku"));

        if (!anggota.cekBatasPinjam()) {
            throw PinjamGagalException.melewatiBatas();
        }

        if (!buku.cekStok()) {
            throw PinjamGagalException.stokHabis();
        }

        TransaksiPinjam transaksi = TransaksiPinjam.buat(anggota, buku);
        transaksiRepository.save(transaksi);

        buku.kurangiStok();
        bukuRepository.save(buku);

        return Bukti.dari(transaksi);
    }
}