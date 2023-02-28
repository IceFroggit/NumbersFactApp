package com.example.numbersfactapp.main.numbers.presentation

import org.junit.Assert.*
import org.junit.Test

class NumbersViewModelTest {
    /**
     * Initial test
     * At start fetch data and show it
     * then try to get some data successfully
     * then re-init and check for result
     */
    @Test
    fun `test init and re-init`() {
        //Подготовка данных
        val communications = TestNumbersCommunications()
        val interactor = TestNumbersIteractor()
        //1. initialize
        val viewModel = NumbersViewModel(communications, interactor)
        interactor.changeExpectedResult(NumbersResult.Success())
        //2. Action
        viewModel.init(isFirstRun = true)
        //3. check Result
        assertEquals(1, communications.progressCalledList.size)
        assertEquals(true, communications.progressCalledList[0])

        assertEquals(2, communications.progressCalledList.size)
        assertEquals(false, communications.progressCalledList[1])

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(UiState.Success(), communications.stateCalledList[0])

        assertEquals(0, communications.numbersList.size)
        assertEquals(1, communications.timesShowList)


        //get some data
        interactor.changeExpectedResult(NumbersResult.Failure())
        viewModel.fetchRandomNumberData()

        assertEquals(3, communications.progressCalledList.size)
        assertEquals(true, communications.progressCalledList[2])

        assertEquals(1, interactor.fetchAboutRandomNumberCalledList.size)

        assertEquals(4, communications.progressCalledList.size)
        assertEquals(false, communications.progressCalledList[3])

        assertEquals(2, communications.stateCalledList.size)
        assertEquals(UiState.Error(/*todo message*/), communications.stateCalledList[0])
        assertEquals(1, communications.timesShowList)

        //При повороте экрана
        viewModel.init(isFirstRun = false)
        assertEquals(4, communications.progressCalledList.size)
        assertEquals(2, communications.stateCalledList.size)
        assertEquals(1, communications.timesShowList)

    }

    /**
     * Try to get information about empty number
     */
    @Test
    fun `fact about empty number`() {
        //Подготовка данных
        val communications = TestNumbersCommunications()
        val interactor = TestNumbersIteractor()
        //1. initialize
        val viewModel = NumbersViewModel(communications, interactor)
        viewModel.fetchFact("")

        assertEquals(0, interactor.fetchAboutRandomNumberCalledList.size)

        assertEquals(0, communications.progressCalledList.size)

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(UiState.Error("entered number is empty"), communications.stateCalledList[0])

        assertEquals(0, communications.timesShowList)

    }

    /**
     * Try to get information about some number
     */
    @Test
    fun `fact about some number`() {
        //Подготовка данных
        val communications = TestNumbersCommunications()
        val interactor = TestNumbersIteractor()
        //1. initialize
        interactor.changeExpectedResult(NumberResult.Success(listOf(Number("45","random fact about 45"))))
        val viewModel = NumbersViewModel(communications, interactor)
        viewModel.fetchFact("45")

        assertEquals(1, communications.progressCalledList.size)
        assertEquals(true, communications.progressCalledList[0])

        assertEquals(1,interactor.fetchAboutRandomNumberCalledList.size)
        assertEquals(Number("45","random fact about 45"), interactor.fetchAboutNumberCalledList[0])

        assertEquals(2, communications.progressCalledList.size)
        assertEquals(false, communications.progressCalledList[1])

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(UiState.Success(), communications.stateCalledList[0])

        assertEquals(1, communications.timesShowList)
        assertEquals(NumberUi("45", "random fact about 45"), communications.numbersList[0])
    }


    private class TestNumbersCommunications : NumbersCommunications {

        val progressCalledList = mutableListOf<Boolean>()
        val stateCalledList = mutableListOf<Boolean>()
        var timesShowList = 0
        val numbersList = mutableListOf<NumberUi>()

        override fun showProgress(show: Boolean) {
            progressCalledList.add(show)
        }

        override fun showState(state: UiState) {
            stateCalledList.add(state)

        }

        override fun showList(list: List<NumberUi>) {
            timesShowList++
            numbersList.addAll(list)
        }
    }

    private class TestNumbersIteractor : NumbersIteractor {
        private val result: NumbersResult = NumbersResult.Success()

        val initCalledList = mutableListOf<NumbersResult>()
        val fetchAboutNumberCalledList = mutableListOf<NumbersResult>()
        val fetchAboutRandomNumberCalledList = mutableListOf<NumbersResult>()

        fun changeExpectedResult(newResult: NumbersResult) {
            result = newResult
        }


        override suspend fun init(): NumbersResult {
            initCalledList.add(result)
            return result
        }

        override suspend fun factAboutNumber(number: String): NumbersResult {
            fetchAboutNumberCalledList.add(result)
            return result
        }

        override suspend fun factAboutRandimNimber(): NumbersResult {
            fetchAboutRandomNumberCalledList.add(result)
            return result
        }

    }
}

