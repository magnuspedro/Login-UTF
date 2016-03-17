package com.atom.magnus.autologin_utf;

/**
 * Created by magnus on 3/9/16.
 */
public class User {
    int _ID;
    String _RA;
    String _Senha;

    public User(){

    }

    public User(int ID, String RA, String Senha){
        this._ID = ID;
        this._RA = RA;
        this._Senha = Senha;
    }

    public User(String RA, String Senha){
        this._RA = RA;
        this._Senha = Senha;
    }

    public int getID(){
        return this._ID;
    }

    public void setID(int ID){
        this._ID = ID;
    }

    public String getRA(){
        return this._RA;
    }

    public void setRA(String RA){
        this._RA = RA;
    }

    public String getSenha(){
        return this._Senha;
    }

    public void setSenha(String Senha){
        this._Senha = Senha;
    }

}
