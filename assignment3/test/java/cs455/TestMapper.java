package cs455;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import record.HousingMedianRecord;


public class TestMapper {

    private static final String TEST_STRING =
            "STF1B   AK1000000000000100010002                                                        9                                        49402                                   1 14772675150222870598Alaska                                                            A0000232608+65285776-151646937 01000550043 000550043000132837000188915000221883000149352000178808000000000000289867000260176000415492000022451000085698000019728000006674000415492000022451000031245000044401000010052000001342000007976000002066000000472000004163000000582000000050000000001000000226000000369000000567000000934000000522000000158000000030000000208000000030000000002000000030000006674000017803000532240000009321000001938000000277000006267000406722000021799000084594000018730000000395000008770000000652000001104000000998000006279000009794000023140000021963000010840000010555000030341000018645000016710000007824000007673000007471000007388000006963000007656000008315000008050000024863000053341000059624000057768000045329000032117000021812000016595000005689000007208000009626000005922000003639000001931000001251000003488000008038000007849000003984000003788000011222000006984000006267000002916000002881000002717000002754000002706000003051000003436000003298000009573000021091000024216000025070000020713000014781000009834000007049000002503000003086000003725000002229000001231000000548000000297000003189000007639000007326000003724000003556000010422000006353000005760000002628000002600000002556000002543000002233000002463000002557000002426000008037000018671000022320000021672000016812000011738000007742000005884000001967000002576000003660000002322000001364000000838000000589000000257000000604000000518000000226000000225000000729000000464000000378000000175000000171000000149000000162000000187000000270000000346000000385000000943000001561000001445000001201000000701000000361000000299000000204000000059000000082000000107000000066000000037000000012000000007000000274000000592000000509000000232000000230000000679000000395000000322000000173000000160000000144000000130000000135000000159000000200000000203000000658000001244000001206000000881000000462000000321000000241000000182000000053000000071000000119000000077000000038000000019000000011000001107000002560000002355000001094000001111000002949000001842000001562000000803000000739000000822000000723000000676000000686000000642000000640000002191000004090000003618000003179000002280000001847000001414000001289000000421000000516000000779000000432000000355000000181000000140000001050000002564000002267000001084000001083000002855000001693000001578000000732000000741000000726000000713000000614000000670000000652000000636000002135000003974000003770000003093000002264000001801000001398000001300000000454000000558000000811000000545000000470000000261000000163000000144000000407000000417000000169000000210000000564000000367000000328000000145000000149000000122000000136000000166000000118000000179000000147000000437000000936000000987000000928000000730000000463000000308000000223000000087000000119000000155000000092000000062000000039000000024000000143000000401000000392000000194000000219000000514000000319000000319000000156000000144000000143000000135000000134000000102000000129000000125000000400000000912000001241000001147000000961000000568000000413000000377000000122000000177000000234000000141000000067000000026000000015000000080000000181000000184000000075000000065000000224000000116000000100000000056000000053000000044000000042000000072000000093000000110000000135000000321000000507000000452000000342000000228000000126000000090000000052000000014000000014000000014000000007000000007000000001000000005000000062000000154000000146000000058000000068000000183000000112000000096000000040000000035000000048000000050000000040000000044000000064000000055000000168000000355000000369000000255000000178000000111000000073000000035000000009000000009000000022000000011000000008000000006000000000000000227000000515000000521000000222000000206000000603000000340000000295000000156000000140000000139000000127000000155000000198000000243000000258000000659000001133000001053000000818000000575000000375000000239000000150000000056000000051000000043000000025000000015000000006000000016000000206000000496000000428000000197000000208000000589000000333000000294000000141000000116000000123000000133000000119000000141000000160000000145000000408000000892000000881000000734000000539000000322000000232000000153000000048000000050000000071000000041000000022000000015000000007000067827000115839000004146000002541000022187000040938000110862000004455000009082000022354000132837000106079000175156000009652000006273000012458000012271000041826000014252000018538000004574000016127         000025601000016225000064720000041359000005749000002780000014625000003604000009413000004839000442455000000333000064975000005811000014668000001178000000223000041104000002718";

    @Test
    public void testHispanicBoundaries() {
        int count = 0;
        for (int i = 3864; i < 3973; i += 9) {
            count++;
        }

        assertThat(count, is(13));

        count = 0;
        for (int i = 4143; i < 4252; i += 9) {
            count++;
        }

        assertThat(count, is(13));

        count = 0;
        for (int i = 3981; i < 4018; i += 9) {
            count++;
        }

        assertThat(count, is(5));

        count = 0;
        for (int i = 4260; i < 4297; i += 9) {
            count++;
        }

        assertThat(count, is(5));

        count = 0;
        for (int i = 4026; i < 4036; i += 9) {
            count++;

        }

        assertThat(count, is(2));

        count = 0;
        for (int i = 4305; i < 4315; i += 9) {
            count++;
        }

        assertThat(count, is(2));
    }

    @Test
    public void testMedianHousingValue() {
        int count = 0;
        for (int i = 2928; i < 3100; i += 9) {
            count++;
        }

        System.out.println(count == HousingMedianRecord.VALUE_LIST.size());
    }

    @Test
    public void testDoublePercentage() {
        double totalPopulation = 12937926;
        double elderlyPopulation = 210110;

        double percentage = elderlyPopulation / totalPopulation * 100;

        System.out.println(percentage);

    }

}
