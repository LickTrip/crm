package com.michal.crm.model.types;

public enum AcademicDegreeTypes {
    Bc("Bc."),
    BcA("BcA."),
    Ing("Ing."),
    IngArch("Ing.arch."),
    Mgr("Mgr."),
    MgA("MgA."),
    MUDr("MUDr."),
    MDDr("MDDr."),
    MVDr("MVDr."),
    JUDr("JUDr."),
    PhDr("PhDr."),
    RNDr("RNDr."),
    PharmDr("PharmDr."),
    ThLic("ThLic."),
    MSDr("MSDr."),
    PaedDr("PaedDr."),
    PhMr("PhMr."),
    RCDr("RCDr."),
    RSDr("RSDr."),
    RTDr("RTDr."),
    ThMgr("ThMgr."),
    doc("doc."),
    prof("prof."),
    PhD("Ph.D."),
    DSc("DSc."),
    CSc("CSc."),
    DrSc("DrSc."),
    ThD("Th.D."),
    as("as."),
    odbas("odb. as."),
    DiS("DiS.");

    private String name;

    AcademicDegreeTypes(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
