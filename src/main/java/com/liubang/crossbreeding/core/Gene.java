package com.liubang.crossbreeding.core;

/**
 * 基因位点。显性（大写）对应的性状、隐性（小写）对应的性状。
 *
 * A/a：多穗（显）/ 少穗（隐）
 * B/b：抗病（显）/ 不抗病（隐）
 * C/c：低饱食（显）/ 高饱食（隐）
 * D/d：低饱和（显）/ 高饱和（隐）
 * E/e：多壳（显）/ 少壳（隐）
 * F/f：生长旺盛（显）/ 生长不旺盛（隐）
 */
public enum Gene {
    A('A', 'a', "多穗", "少穗"),
    B('B', 'b', "抗病", "不抗病"),
    C('C', 'c', "低饱食", "高饱食"),
    D('D', 'd', "低饱和", "高饱和"),
    E('E', 'e', "多壳", "少壳"),
    F('F', 'f', "生长旺盛", "生长不旺盛");

    public final char dominant;
    public final char recessive;
    public final String dominantName;
    public final String recessiveName;

    Gene(char dominant, char recessive, String dominantName, String recessiveName) {
        this.dominant = dominant;
        this.recessive = recessive;
        this.dominantName = dominantName;
        this.recessiveName = recessiveName;
    }

    public static Gene fromAllele(char allele) {
        char upper = Character.toUpperCase(allele);
        for (Gene g : values()) {
            if (g.dominant == upper) return g;
        }
        throw new IllegalArgumentException("Unknown allele: " + allele);
    }
}
