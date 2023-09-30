package main.java.com.crud.application;

import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import main.java.com.crud.dao.Indice;
import main.java.com.crud.dao.RegistroDAO;

import main.java.com.crud.model.Registro;

public class CRUD<T extends RegistroDAO> {
    Constructor<T> construtor;

    public CRUD(Constructor<T> construtor) {
        this.construtor = construtor;
    }

    private final String indiceFileName = "src\\main\\java\\com\\crud\\db\\Registro.db";

    public void Create(Registro novoRegistro) {
        byte[] b;

        try {
            Indice indice = new Indice();
            RandomAccessFile arq = new RandomAccessFile(
                    indiceFileName, "rw");
            byte id = 1;
            arq.seek(0);
            if (arq.length() == 0) {
                arq.writeByte(id);
            } else {
                id = arq.readByte();
                id++;
                arq.seek(0);
                arq.writeByte(id);
            }
            arq.seek(arq.length());
            long posAtual = arq.getFilePointer();
            arq.writeByte(' ');
            b = novoRegistro.toByteArray();
            arq.writeInt(b.length);
            arq.writeByte(id);
            arq.write(b);
            arq.close();
            indice.insereRegistro(id, posAtual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Registro Read(byte id) {
        byte[] b;
        Registro registroProcurado = new Registro();
        int tam;
        Indice indice = new Indice();
        try {
            RandomAccessFile arq = new RandomAccessFile(
                    indiceFileName, "rw");
            long pos = indice.lerRegistro(id);
            if (pos == -1) {
                arq.close();
                registroProcurado.idProjeto = -1;
                return registroProcurado;
            }
            arq.seek(pos);
            if (arq.readByte() == ' ') {
                tam = arq.readInt();
                b = new byte[tam + 1];
                arq.read(b);
                registroProcurado.fromByteArray(b);

                if (registroProcurado.idProjeto == id) {
                    arq.close();
                    return registroProcurado;
                }
            } else {
                arq.close();
                registroProcurado.idProjeto = -1;
                return registroProcurado;
            }
            arq.close();
            registroProcurado.idProjeto = -1;
            return registroProcurado;
        } catch (

        Exception e) {
            e.printStackTrace();
            Registro lixo = new Registro();
            lixo.idProjeto = -1;
            return lixo;
        }
    }

    public void Update(Registro registro) {
        byte[] b;
        byte[] b2;
        Registro registroProcurado = new Registro();
        int tam;
        long p;
        long pLap;
        Indice index = new Indice();

        try {
            RandomAccessFile arq = new RandomAccessFile(
                    indiceFileName, "rw");
            ;
            arq.seek(0);
            arq.readByte();
            long pos = index.lerRegistro(registro.idProjeto);
            System.out.println(pos);
            if (pos == -1) {
                arq.close();
                registroProcurado.idProjeto = -1;
            } else {
                arq.seek(pos);
                pLap = arq.getFilePointer();
                if (arq.readByte() == ' ') {
                    tam = arq.readInt();
                    p = arq.getFilePointer();
                    b = new byte[tam + 1];
                    arq.read(b);
                    registroProcurado.fromByteArray(b);
                    if (registroProcurado.idProjeto == registro.idProjeto) {
                        b2 = registro.toByteArray();
                        if (tam >= b2.length) {
                            arq.seek(p);
                            arq.writeByte(registro.idProjeto);
                            arq.write(b2);
                        } else {
                            arq.seek(pLap);
                            arq.writeByte('*');
                            arq.seek(arq.length());
                            pLap = arq.getFilePointer();
                            arq.writeByte(' ');
                            arq.writeInt(b2.length);
                            arq.writeByte(registro.idProjeto);
                            arq.write(b2);
                            index.atualizaRegistro(registro.idProjeto, pLap);
                        }
                    }
                }
            }

            while (arq.getFilePointer() < arq.length()) {
                pLap = arq.getFilePointer();
                if (arq.readByte() == ' ') {
                    tam = arq.readInt();
                    p = arq.getFilePointer();
                    b = new byte[tam + 1];
                    arq.read(b);
                    registroProcurado.fromByteArray(b);
                    if (registroProcurado.idProjeto == registro.idProjeto) {
                        b2 = registro.toByteArray();
                        if (tam >= b2.length) {
                            arq.seek(p);
                            arq.writeByte(registro.idProjeto);
                            arq.write(b2);
                        } else {
                            arq.seek(pLap);
                            arq.writeByte('*');
                            arq.seek(arq.length());
                            arq.writeByte(' ');
                            arq.writeInt(b2.length);
                            arq.writeByte(registro.idProjeto);
                            arq.write(b2);
                        }
                        break;
                    }
                } else {
                    tam = arq.readInt();
                    b = new byte[tam + 1];
                    arq.read(b);
                }
            }
            arq.close();
            if (registroProcurado.idProjeto == registro.idProjeto) {
                System.out.println("REGISTRO ALTERADO");
            } else
                System.out.println("REGISTRO NÃO ENCONTRADO");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("REGISTRO NÃO ENCONTRADO");
        }
    }

    public Registro Delete(byte id) {
        byte[] b;
        Registro registroProcurado = new Registro();
        int tam;
        long p;
        Indice index = new Indice();
        try {
            RandomAccessFile arq = new RandomAccessFile(
                    indiceFileName, "rw");
            arq.seek(0);
            if (arq.readByte() < id) {
                arq.close();
                throw new Exception();
            }
            long pos = index.lerRegistro(id);
            System.out.println(pos);
            if (pos == -1) {
                arq.close();
                registroProcurado.idProjeto = -1;
                return registroProcurado;
            }
            arq.seek(pos);
            p = arq.getFilePointer();
            if (arq.readByte() == ' ') {
                tam = arq.readInt();
                b = new byte[tam + 1];
                arq.read(b);
                registroProcurado.fromByteArray(b);
                System.out.println(registroProcurado.idProjeto);
                if (registroProcurado.idProjeto == id) {
                    arq.seek(p);
                    arq.writeByte('*');
                    arq.close();
                    index.deletaRegistro(id);
                    return registroProcurado;
                }
            } else {
                arq.close();
                registroProcurado.idProjeto = -1;
                return registroProcurado;
            }
            arq.close();
            registroProcurado.idProjeto = -1;
            return registroProcurado;
        } catch (Exception e) {
            e.printStackTrace();
            Registro lixo = new Registro();
            lixo.idProjeto = -1;
            return lixo;
        }
    }

    public List<Registro> listarTodosRegistros() {
        List<Registro> registros = new ArrayList<>();
        byte[] b;
        Registro registroProcurado;
        int tam;
        long p;

        try {
            RandomAccessFile arq = new RandomAccessFile(
                    indiceFileName, "rw");
            arq.seek(0);
            byte id = arq.readByte();

            while (arq.getFilePointer() < arq.length()) {
                p = arq.getFilePointer();
                if (arq.readByte() == ' ') {
                    tam = arq.readInt();
                    b = new byte[tam + 1];
                    arq.read(b);
                    registroProcurado = new Registro();
                    registroProcurado.setIdProjeto(id);
                    registroProcurado.fromByteArray(b);

                    if (registroProcurado.idProjeto != -1) {
                        registros.add(registroProcurado);
                    }
                } else {
                    tam = arq.readInt();
                    b = new byte[tam + 1];
                    arq.read(b);
                }
            }

            arq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return registros;
    }

}
