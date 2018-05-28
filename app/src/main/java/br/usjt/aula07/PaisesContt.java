package br.usjt.aula07;

import android.provider.BaseColumns;

/**
 * Created by Lucas De Vita on 24/04/18.
 * RA: 81617007
 */
public class PaisesContt {

    public static abstract class PaisEntry implements BaseColumns {
        public static final String TABLE_NAME = "pais";
        public static final String COLUMN_NAME_NOME = "nome";
        public static final String COLUMN_NAME_REGIAO = "regiao";
        public static final String COLUMN_NAME_CAPITAL = "capital";
        public static final String COLUMN_NAME_BANDEIRA = "bandeira";
        public static final String COLUMN_NAME_CODIGO3 = "codigo3";


    }
}
