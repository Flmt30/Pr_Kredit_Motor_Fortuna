package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

public class Kredit extends Koneksi {
    private long id;
    Server server = new Server();
    String SERVER = server.urlDatabase1();
    String URL = "http://" + SERVER + "/jskreditmotor/tbkredit.php";
    String url = "";
    String response = "";

    public String tampil_query_kredit() {
        try {
            url = URL + "?operasi=view";
            System.out.println("URL tampil_query_kredit: " + url);
            response = call(url);
        } catch (Exception e) {
            response = e.toString();
        }
        return response;
    }

    public String simpan_kredit(String idkreditor, String kdmotor, String hrgtunai, String dp, String hrgkredit, String bunga, String lama, String totalkredit, String angsuran) {
        try {
            // Kita sesuaikan dengan parameter yang dikirim oleh PengajuanKreditActivity
            url = URL + "?operasi=insert" +
                    "&idkreditor=" + URLEncoder.encode(idkreditor, StandardCharsets.UTF_8.name()) +
                    "&kdmotor=" + URLEncoder.encode(kdmotor, StandardCharsets.UTF_8.name()) + 
                    "&hrgtunai=" + URLEncoder.encode(hrgtunai, StandardCharsets.UTF_8.name()) + 
                    "&dp=" + URLEncoder.encode(dp, StandardCharsets.UTF_8.name()) +
                    "&hrgkredit=" + URLEncoder.encode(hrgkredit, StandardCharsets.UTF_8.name()) + 
                    "&bunga=" + URLEncoder.encode(bunga, StandardCharsets.UTF_8.name()) + 
                    "&lama=" + URLEncoder.encode(lama, StandardCharsets.UTF_8.name()) +
                    "&totalkredit=" + URLEncoder.encode(totalkredit, StandardCharsets.UTF_8.name()) +
                    "&angsuran=" + URLEncoder.encode(angsuran, StandardCharsets.UTF_8.name());
            System.out.println("URL simpan_kredit : " + url);
            response = call(url);
        } catch (Exception e) {
            response = e.toString();
        }
        return response;
    }

    public String hapus_kredit(String invoice) {
        try {
            url = URL + "?operasi=delete&invoice=" + invoice;
            System.out.println("URL Hapus kredit : " + url);
            response = call(url);
        } catch (Exception e) {
            response = e.toString();
        }
        return response;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}