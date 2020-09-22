package com.crp.infokajianislami;

import java.net.URI;

public class Peserta {
    private String id_peserta, nama_peserta, email_peserta;
    private URI foto_peserta;


    public Peserta(){

    }

    public String getId_peserta() {
        return id_peserta;
    }

    public void setId_peserta(String id_peserta) {
        this.id_peserta = id_peserta;
    }

    public String getNama_peserta() {
        return nama_peserta;
    }

    public void setNama_peserta(String nama_peserta) {
        this.nama_peserta = nama_peserta;
    }

    public String getEmail_peserta() {
        return email_peserta;
    }

    public void setEmail_peserta(String email_peserta) {
        this.email_peserta = email_peserta;
    }

    public URI getFoto_peserta() {
        return foto_peserta;
    }

    public void setFoto_peserta(URI foto_peserta) {
        this.foto_peserta = foto_peserta;
    }
}
