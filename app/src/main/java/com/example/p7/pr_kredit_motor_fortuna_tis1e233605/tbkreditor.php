<?php
header('Content-Type: application/json');

$server = "localhost";
$user   = "root";
$pass   = "";
$db     = "Db_Fortuna";

$conn = mysqli_connect($server, $user, $pass, $db);

if (!$conn) {
    echo "Koneksi Gagal";
    exit();
}

$operasi = isset($_GET['operasi']) ? $_GET['operasi'] : '';

switch ($operasi) {
    case "view":
        // Menampilkan semua data kreditor
        $query = mysqli_query($conn, "SELECT * FROM tbkreditor ORDER BY idkreditor DESC");
        $data = array();
        while ($row = mysqli_fetch_assoc($query)) {
            $data[] = $row;
        }
        echo json_encode($data);
        break;

    case "insert":
        // Mengambil data dari URL (GET)
        $kdkreditor = isset($_GET['kdkreditor']) ? $_GET['kdkreditor'] : '';
        $nama       = isset($_GET['nama']) ? $_GET['nama'] : '';
        $alamat     = isset($_GET['alamat']) ? $_GET['alamat'] : '';
        $telp       = isset($_GET['telp']) ? $_GET['telp'] : '';
        $pekerjaan  = isset($_GET['pekerjaan']) ? $_GET['pekerjaan'] : '';

        $sql = "INSERT INTO tbkreditor (kdkreditor, nama, alamat, telp, pekerjaan) 
                VALUES ('$kdkreditor', '$nama', '$alamat', '$telp', '$pekerjaan')";
        $query = mysqli_query($conn, $sql);
        
        if ($query) {
            echo "Berhasil Simpan Kreditor";
        } else {
            echo "Gagal Simpan: " . mysqli_error($conn);
        }
        break;

    case "get_kreditor_by_idkreditor":
        // Mengambil detail satu kreditor berdasarkan ID
        $idkreditor = isset($_GET['idkreditor']) ? $_GET['idkreditor'] : '';
        $query = mysqli_query($conn, "SELECT * FROM tbkreditor WHERE idkreditor = '$idkreditor'");
        $data = array();
        while ($row = mysqli_fetch_assoc($query)) {
            $data[] = $row;
        }
        echo json_encode($data);
        break;

    case "update":
        // Mengupdate data kreditor
        $idkreditor = isset($_GET['idkreditor']) ? $_GET['idkreditor'] : '';
        $kdkreditor = isset($_GET['kdkreditor']) ? $_GET['kdkreditor'] : '';
        $nama       = isset($_GET['nama']) ? $_GET['nama'] : '';
        $alamat     = isset($_GET['alamat']) ? $_GET['alamat'] : '';
        $telp       = isset($_GET['telp']) ? $_GET['telp'] : '';
        $pekerjaan  = isset($_GET['pekerjaan']) ? $_GET['pekerjaan'] : '';

        $sql = "UPDATE tbkreditor SET 
                kdkreditor='$kdkreditor', 
                nama='$nama', 
                alamat='$alamat', 
                telp='$telp', 
                pekerjaan='$pekerjaan' 
                WHERE idkreditor='$idkreditor'";
        
        $query = mysqli_query($conn, $sql);
        if ($query) {
            echo "Berhasil Update Kreditor";
        } else {
            echo "Gagal Update: " . mysqli_error($conn);
        }
        break;

    case "delete":
        // Menghapus data kreditor
        $idkreditor = isset($_GET['idkreditor']) ? $_GET['idkreditor'] : '';
        $sql = "DELETE FROM tbkreditor WHERE idkreditor = '$idkreditor'";
        $query = mysqli_query($conn, $sql);
        
        if ($query) {
            echo "Berhasil Hapus Kreditor";
        } else {
            echo "Gagal Hapus";
        }
        break;

    default:
        echo "Operasi Tidak Ditemukan";
        break;
}

mysqli_close($conn);
?>
