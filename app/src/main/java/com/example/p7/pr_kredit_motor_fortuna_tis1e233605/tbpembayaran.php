{\rtf1\ansi\ansicpg1252\cocoartf2822
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
{\*\listtable{\list\listtemplateid1\listhybrid{\listlevel\levelnfc23\levelnfcn23\leveljc0\leveljcn0\levelfollow0\levelstartat1\levelspace360\levelindent0{\*\levelmarker \{hyphen\}}{\leveltext\leveltemplateid1\'01\uc0\u8259 ;}{\levelnumbers;}\fi-360\li720\lin720 }{\listname ;}\listid1}}
{\*\listoverridetable{\listoverride\listid1\listoverridecount0\ls1}}
\paperw11900\paperh16840\margl1440\margr1440\vieww11400\viewh18600\viewkind0
\pard\tx220\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\li720\fi-720\pardirnatural\partightenfactor0
\ls1\ilvl0
\f0\fs24 \cf0 {\listtext	\uc0\u8259 	}<?php\
{\listtext	\uc0\u8259 	}header('Content-Type: application/json');\
{\listtext	\uc0\u8259 	}include "koneksi.php";\
\
{\listtext	\uc0\u8259 	}$koneksi = $con;\
\
{\listtext	\uc0\u8259 	}if (!$koneksi) \{\
{\listtext	\uc0\u8259 	}    die(json_encode(["error" => "Koneksi database gagal"]));\
{\listtext	\uc0\u8259 	}\}\
\
{\listtext	\uc0\u8259 	}$operasi = $_GET['operasi'] ?? '';\
\
{\listtext	\uc0\u8259 	}switch ($operasi) \{\
{\listtext	\uc0\u8259 	}    case "query_pembayaran":\
{\listtext	\uc0\u8259 	}        $sql = "SELECT p.*, kr.nama as namakreditor \
{\listtext	\uc0\u8259 	}                FROM tb_pembayaran p \
{\listtext	\uc0\u8259 	}                LEFT JOIN kredit k ON p.invoice = k.idkredit \
{\listtext	\uc0\u8259 	}                LEFT JOIN tbkreditor kr ON k.idkreditor = kr.idkreditor \
{\listtext	\uc0\u8259 	}                ORDER BY p.idbayar DESC";\
{\listtext	\uc0\u8259 	}        $query = mysqli_query($koneksi, $sql);\
{\listtext	\uc0\u8259 	}        $result = array();\
{\listtext	\uc0\u8259 	}        if ($query) \{\
{\listtext	\uc0\u8259 	}            while ($row = mysqli_fetch_assoc($query)) \{\
{\listtext	\uc0\u8259 	}                $result[] = $row;\
{\listtext	\uc0\u8259 	}            \}\
{\listtext	\uc0\u8259 	}        \}\
{\listtext	\uc0\u8259 	}        echo json_encode($result);\
{\listtext	\uc0\u8259 	}        break;\
\
{\listtext	\uc0\u8259 	}    case "simpan_pembayaran":\
{\listtext	\uc0\u8259 	}        $invoice = $_GET['invoice'] ?? '';\
{\listtext	\uc0\u8259 	}        $tglbayar = $_GET['tglbayar'] ?? '';\
{\listtext	\uc0\u8259 	}        $angsuranke = $_GET['angsuranke'] ?? '';\
{\listtext	\uc0\u8259 	}        $bayar = $_GET['bayar'] ?? '';\
\
{\listtext	\uc0\u8259 	}        if (empty($invoice) || empty($tglbayar) || empty($angsuranke)) \{\
{\listtext	\uc0\u8259 	}            echo "Gagal: Data tidak lengkap";\
{\listtext	\uc0\u8259 	}            break;\
{\listtext	\uc0\u8259 	}        \}\
\
{\listtext	\uc0\u8259 	}        $sql = "INSERT INTO tb_pembayaran (invoice, tglbayar, angsuranke, bayar) \
{\listtext	\uc0\u8259 	}                VALUES ('$invoice', '$tglbayar', '$angsuranke', '$bayar')";\
{\listtext	\uc0\u8259 	}        \
{\listtext	\uc0\u8259 	}        if (mysqli_query($koneksi, $sql)) \{\
{\listtext	\uc0\u8259 	}            echo "Berhasil Simpan Pembayaran";\
{\listtext	\uc0\u8259 	}        \} else \{\
{\listtext	\uc0\u8259 	}            echo "Gagal SQL: " . mysqli_error($koneksi);\
{\listtext	\uc0\u8259 	}        \}\
{\listtext	\uc0\u8259 	}        break;\
\
{\listtext	\uc0\u8259 	}    case "cek_status_angsuran":\
{\listtext	\uc0\u8259 	}        $invoice = $_GET['invoice'] ?? '';\
{\listtext	\uc0\u8259 	}        $q_terakhir = mysqli_query($koneksi, "SELECT MAX(angsuranke) as terakhir FROM tb_pembayaran WHERE invoice = '$invoice'");\
{\listtext	\uc0\u8259 	}        $data_terakhir = mysqli_fetch_assoc($q_terakhir);\
{\listtext	\uc0\u8259 	}        $terakhir = (int)($data_terakhir['terakhir'] ?? 0);\
\
{\listtext	\uc0\u8259 	}        $q_kredit = mysqli_query($koneksi, "SELECT lama_cicilan, angsuran FROM kredit WHERE idkredit = '$invoice'");\
{\listtext	\uc0\u8259 	}        $data_kredit = mysqli_fetch_assoc($q_kredit);\
{\listtext	\uc0\u8259 	}        \
{\listtext	\uc0\u8259 	}        echo json_encode([\
{\listtext	\uc0\u8259 	}            "angsuran_terakhir" => $terakhir,\
{\listtext	\uc0\u8259 	}            "total_tenor" => (int)($data_kredit['lama_cicilan'] ?? 0),\
{\listtext	\uc0\u8259 	}            "nominal_angsuran" => $data_kredit['angsuran'] ?? "0"\
{\listtext	\uc0\u8259 	}        ]);\
{\listtext	\uc0\u8259 	}        break;\
\
{\listtext	\uc0\u8259 	}    case "hapus_pembayaran":\
{\listtext	\uc0\u8259 	}        $id = $_GET['idbayar'] ?? '';\
{\listtext	\uc0\u8259 	}        $query = mysqli_query($koneksi, "DELETE FROM tb_pembayaran WHERE idbayar = '$id'");\
{\listtext	\uc0\u8259 	}        echo $query ? "Berhasil Hapus" : "Gagal Hapus";\
{\listtext	\uc0\u8259 	}        break;\
{\listtext	\uc0\u8259 	}\}\
{\listtext	\uc0\u8259 	}?>\
}