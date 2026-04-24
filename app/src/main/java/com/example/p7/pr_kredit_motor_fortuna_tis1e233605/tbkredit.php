<?php
header('Content-Type: application/json');
$conn = mysqli_connect("localhost", "root", "", "Db_Fortuna");

if (!$conn) {
    echo "Koneksi Gagal";
    exit();}

$operasi = isset($_GET['operasi']) ? $_GET['operasi'] : '';

switch ($operasi) {
    case "view":
        // Query Lengkap dengan JOIN dan Alias yang sesuai dengan Java
        $sql = "SELECT 
                    k.idkredit as invoice, 
                    k.tgl_kredit as tanggal, 
                    k.idkreditor, 
                    kr.nama as nama, 
                    m.kdmotor as kdmotor,
                    m.nama as nmotor, 
                    m.harga as hrgtunai, 
                    k.uang_muka as dp, 
                    (m.harga - k.uang_muka) as hrgkredit, 
                    k.bunga, 
                    k.lama_cicilan as lama, 
                    (k.angsuran * k.lama_cicilan) as totalkredit, 
                    k.angsuran 
                FROM kredit k
                JOIN tbkreditor kr ON k.idkreditor = kr.idkreditor
                JOIN motor m ON k.idmotor = m.idmotor
                ORDER BY k.idkredit DESC";
        
        $query = mysqli_query($conn, $sql);
        $data = array();
        while ($row = mysqli_fetch_assoc($query)) {
            $data[] = $row;
        }
        echo json_encode($data);
        break;

    case "insert":
        $idkreditor   = $_GET['idkreditor'];
        $kdmotor      = $_GET['kdmotor'];
        $uang_muka    = $_GET['dp'];
        $bunga        = $_GET['bunga'];
        $lama_cicilan = $_GET['lama'];
        $angsuran     = $_GET['angsuran'];
        
        // Cari idmotor berdasarkan kdmotor string (misal M001)
        $resMotor = mysqli_query($conn, "SELECT idmotor FROM motor WHERE kdmotor = '$kdmotor'");
        $rowMotor = mysqli_fetch_assoc($resMotor);
        $idmotor  = $rowMotor ? $rowMotor['idmotor'] : 0;

        $tgl_kredit = date('Y-m-d');
        
        $sql = "INSERT INTO kredit (idkreditor, idmotor, idpetugas, tgl_kredit, fotocopy_ktp, fotocopy_kk, slip_gaji, uang_muka, bunga, lama_cicilan, angsuran) 
                VALUES ('$idkreditor', '$idmotor', '1', '$tgl_kredit', 'Ada', 'Ada', 'Ada', '$uang_muka', '$bunga', '$lama_cicilan', '$angsuran')";
        
        $query = mysqli_query($conn, $sql);
        if ($query) {
            echo "Berhasil Simpan Pengajuan Kredit";
        } else {
            echo "Gagal: " . mysqli_error($conn);
        }
        break;

    case "delete":
        $invoice = $_GET['invoice'];
        $sql = "DELETE FROM kredit WHERE idkredit = '$invoice'";
        $query = mysqli_query($conn, $sql);
        echo ($query ? "Berhasil Hapus" : "Gagal Hapus");
        break;
}
mysqli_close($conn);
?>
