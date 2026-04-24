<?php
// tbpetugas.php
$server = "localhost";
$user = "root";
$pass = "";
$db = "Db_Fortuna";
$conn = mysqli_connect($server, $user, $pass, $db);

@$operasi = $_GET['operasi'];

switch ($operasi) {
    case "view":
        $q = mysqli_query($conn, "SELECT * FROM petugas ORDER BY idpetugas DESC");
        $res = array();
        while($r = mysqli_fetch_assoc($q)) $res[] = $r;
        echo json_encode($res);
        break;

    case "insert":
        $kdpetugas = $_GET['kdpetugas'];
        $nama = $_GET['nama'];
        $jabatan = $_GET['jabatan'];
        $q = mysqli_query($conn, "INSERT INTO petugas (kdpetugas, nama, jabatan) VALUES ('$kdpetugas', '$nama', '$jabatan')");
        echo $q ? "Data Berhasil Disimpan" : "Gagal Simpan: " . mysqli_error($conn);
        break;

    case "get_petugas_by_kdpetugas":
        $idpetugas = $_GET['idpetugas'];
        $q = mysqli_query($conn, "SELECT * FROM petugas WHERE idpetugas='$idpetugas'");
        echo "[" . json_encode(mysqli_fetch_assoc($q)) . "]";
        break;

    case "update":
        $idpetugas = $_GET['idpetugas'];
        $kdpetugas = $_GET['kdpetugas'];
        $nama = $_GET['nama'];
        $jabatan = $_GET['jabatan'];
        $q = mysqli_query($conn, "UPDATE petugas SET kdpetugas='$kdpetugas', nama='$nama', jabatan='$jabatan' WHERE idpetugas='$idpetugas'");
        echo $q ? "Update Data Berhasil" : "Gagal Update: " . mysqli_error($conn);
        break;

    case "delete":
        $idpetugas = $_GET['idpetugas'];
        $q = mysqli_query($conn, "DELETE FROM petugas WHERE idpetugas='$idpetugas'");
        echo $q ? "Delete Data Berhasil" : "Gagal Hapus";
        break;
}
?>
