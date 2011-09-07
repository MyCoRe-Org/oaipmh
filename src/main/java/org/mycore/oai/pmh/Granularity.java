package org.mycore.oai.pmh;

/**
 * Date granularity. OAI PMH supports UTC date format in form of YYYY-MM-DD and YYYY-MM-DD HH:MM:SS.
 * 
 * @author Matthias Eichner
 */
public enum Granularity {

    YYYY_MM_DD, YYYY_MM_DD_THH_MM_SS_Z, AUTO;

}
