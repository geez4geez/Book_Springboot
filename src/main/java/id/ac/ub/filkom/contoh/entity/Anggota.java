package id.ac.ub.filkom.contoh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/** «entity» Anggota: satu objek Anggota = satu baris di tabel "anggota". */
@Entity
@Table(name = "anggota")
public class Anggota {

    @Id
    @Column(name = "id_anggota", length = 10)
    private String idAnggota;

    @Column(name = "nama", length = 100, nullable = false)
    private String nama;

    @Column(name = "batas_pinjam")
    private int batasPinjam;

    /** Relasi 1 -- 0..* ke TransaksiPinjam. */
    @OneToMany(mappedBy = "anggota")
    private List<TransaksiPinjam> daftarPinjam = new ArrayList<>();

    protected Anggota() { }            // wajib ada untuk JPA

    public Anggota(String idAnggota, String nama, int batasPinjam) {
        this.idAnggota = idAnggota;
        this.nama = nama;
        this.batasPinjam = batasPinjam;
    }

    /** +cekBatasPinjam() : boolean */
    public boolean cekBatasPinjam() {
        long sedangDipinjam = daftarPinjam.stream()
                .filter(TransaksiPinjam::isDipinjam)
                .count();
        return sedangDipinjam < batasPinjam;
    }

    /** Menjaga konsistensi relasi dua arah. */
    void catatPinjaman(TransaksiPinjam transaksi) {
        daftarPinjam.add(transaksi);
    }

    public String getIdAnggota() { return idAnggota; }
    public String getNama()      { return nama; }
    public int getBatasPinjam()  { return batasPinjam; }
}