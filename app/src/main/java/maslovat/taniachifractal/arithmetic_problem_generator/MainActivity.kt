package maslovat.taniachifractal.arithmetic_problem_generator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import maslovat.taniachifractal.ariithmetic_problem_generator_and_verifier.DIVIDE_ID
import maslovat.taniachifractal.ariithmetic_problem_generator_and_verifier.GameStats
import maslovat.taniachifractal.ariithmetic_problem_generator_and_verifier.PLUS_ID
import maslovat.taniachifractal.ariithmetic_problem_generator_and_verifier.Problem
import maslovat.taniachifractal.arithmetic_problem_generator.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    /** Link to the main layout activity_main.xml; "fld" means "field"*/
    private lateinit var fld: ActivityMainBinding
    /**The problem that is shown*/
    private lateinit var problem: Problem
    /**Game Stats*/
    private lateinit var gameStats: GameStats
    /**Load*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // convert xml elements to widgets kotlin can work with
        fld = ActivityMainBinding.inflate(layoutInflater)
        setContentView(fld.root)

        fld.btStart.setOnClickListener{ btStart_Click() }
        fld.btEnter.setOnClickListener { btEnter_Click() }

        gameStats = GameStats()
        loadStats()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // save problem
        outState.putInt("1st",problem.firstNum)
        outState.putInt("op",problem.operator)
        outState.putInt("2nd",problem.secondNum)
        // save stats
        outState.putInt("crt",gameStats.solvedCorrect)
        outState.putInt("inc",gameStats.solvedIncorrect)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // load problem
        problem = Problem(
            savedInstanceState.getInt("1st"),
            savedInstanceState.getInt("op"),
            savedInstanceState.getInt("2nd")
        )
        // load stats
        gameStats = GameStats()
        gameStats.solvedCorrect = savedInstanceState.getInt("crt")
        gameStats.solvedIncorrect = savedInstanceState.getInt("inc")

        loadStats()
        loadProblem()
        initGameFields()
    }

    /**Put stat data to text boxes*/
    private fun loadStats()
    {
        fld.tbPercentSolvedCorrectly.text = String.format("%.1f",gameStats.getPercentSolvedCorrect())
        fld.tbProblemsSolvedCount.text = gameStats.getProblemSolvedCount().toString()
        fld.tbSolvedCorrectly.text = gameStats.solvedCorrect.toString()
        fld.tbSolvedIncorrectly.text = gameStats.solvedIncorrect.toString()
    }
    /**Init problem data*/
    private fun generateProblem()
    {
        problem = Problem(
            Random.nextInt(10,99),
            Random.nextInt(PLUS_ID, DIVIDE_ID +1),
            Random.nextInt(10,99)
            )
    }
    /**Put problem data to text boxes*/
    private fun loadProblem()
    {
        fld.firstNum.text = problem.getFirstNum()
        fld.secondNum.text = problem.getSecondNum()
        fld.operator.text = problem.getOperator()
        fld.btStart.text = problem.getSolution()
    }
    /**Check if input solution is correct*/
    private fun solutionIsCorrect():Boolean
    {
        var tempInputStr = fld.solutionInput.text.toString()
        tempInputStr = tempInputStr.replace(",",".")
        tempInputStr = tempInputStr.replace("+","")
        var tempInput = 0.0
        try
        {
            tempInput = tempInputStr.toDouble()
        }
        catch (e: Exception)
        {
            return false
        }
        return tempInput == problem.getSolution().toDouble()
    }
    /**Init game fields*/
    private fun initGameFields()
    {
        fld.solutionInput.isEnabled=true
        fld.btEnter.isEnabled=true
        fld.solutionInput.setText("")
    }
    /**Start game*/
    private fun btStart_Click()
    {
        generateProblem();loadProblem();initGameFields()
    }
    /**Input and check*/
    private fun btEnter_Click()
    {
        if (fld.solutionInput.text.toString()=="") return
        if (solutionIsCorrect()) gameStats.solvedCorrect++
        else gameStats.solvedIncorrect++
        generateProblem()
        loadProblem()
        loadStats()
        initGameFields()
    }
}