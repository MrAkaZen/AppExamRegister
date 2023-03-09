package university;

public class Subject {
    private String _nameSubj;
    private String _codSubj;

    public Subject(String nameSubj, String codSubj) {
        this._nameSubj = nameSubj;
        this._codSubj = codSubj;
    }

    public String getNameSubj() {
        return _nameSubj;
    }

    public void setNameSubj(String nameSubj) {
        this._nameSubj = nameSubj;
    }

    public String getCodSubj() {
        return _codSubj;
    }

    public void setCodSubj(String codSubj) {
        this._codSubj = codSubj;
    }

    @Override
    public String toString() {
        return "Subject: " + _nameSubj + ", " + _codSubj;
    }
}
