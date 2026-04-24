<?php
header('Content-Type: application/json');
$conn = mysqli_connect("localhost", "root", "", "Db_Fortuna");

if (!$conn) {
    echo "Koneksi Gagal";
    exit();
}

$operasi = isset($_GET['operasi']) ? $_GET['operasi'] : '';

switch ($operasi) {
    case "view":
        $query = mysqli_query($conn, "SELECT * FROM motor ORDER BY idmotor DESC");
        $data = array();
        while ($row = mysqli_fetch_assoc($query)) { $data[] = $row; }
        echo json_encode($data);
        break;

    case "insert":
        $kdmotor = $_GET['kdmotor'];
        $nama    = $_GET['nama'];
        $tipe    = $_GET['tipe'];
        $harga   = $_GET['harga'];
        $sql = "INSERT INTO motor (kdmotor, nama, tipe, harga) VALUES ('$kdmotor', '$nama', '$tipe', '$harga')";
        $query = mysqli_query($conn, $sql);
        echo ($query ? "Berhasil Simpan Data Motor" : "Gagal: " . mysqli_error($conn));
        break;

    case "get_motor_by_idmotor": // HARUS SAMA DENGAN JAVA
        $idmotor = $_GET['idmotor'];
        $query = mysqli_query($conn, "SELECT * FROM motor WHERE idmotor = '$idmotor'");
        $data = array();
        while ($row = mysqli_fetch_assoc($query)) { $data[] = $row; }
        echo json_encode($data);
        break;

    case "get_motor_by_kdmotor": // Untuk pengajuan kredit
        $kdmotor = $_GET['kdmotor'];
        $query = mysqli_query($conn, "SELECT * FROM motor WHERE kdmotor = '$kdmotor'");
        $data = array();
        while ($row = mysqli_fetch_assoc($query)) { $data[] = $row; }
        echo json_encode($data);
        break;

    case "update":
        $idmotor = $_GET['idmotor'];
        $kdmotor = $_GET['kdmotor'];
        $nama    = $_GET['nama'];
        $tipe    = $_GET['tipe'];
        $harga   = $_GET['harga'];
        $sql = "UPDATE motor SET kdmotor='$kdmotor', nama='$nama', tipe='$tipe', harga='$harga' WHERE idmotor='$idmotor'";
        $query = mysqli_query($conn, $sql);
        echo ($query ? "Berhasil Update" : "Gagal Update");
        break;

    case "delete":
        $idmotor = $_GET['idmotor'];
        $sql = "DELETE FROM motor WHERE idmotor = '$idmotor'";
        $query = mysqli_query($conn, $sql);
        echo ($query ? "Berhasil Hapus" : "Gagal Hapus");
        break;
}
mysqli_close($conn);
?>