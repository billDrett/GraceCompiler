package compiler;

/**
 * Created by bill on 17/05/17.
 */
public class Quad
{
    private int label;
    private String operator;
    private String opt1, opt2, opt3;

    public Quad(int lab, String oper, String option1, String option2, String option3)
    {
        label = lab;
        operator = new String(oper);
        opt1 = new String(option1);
        opt2 = new String(option2);
        opt3 = new String(option3);
    }

    public void setOpt3(String option3)
    {
        opt3 = option3;
    }

    public String getOperator()
    {
        return operator;
    }

    public String getOpt1()
    {
        return opt1;
    }

    public String getOpt2()
    {
        return opt2;
    }

    public String getOpt3()
    {
        return opt3;
    }

    public int getLabel()
    {
        return label;
    }
}
