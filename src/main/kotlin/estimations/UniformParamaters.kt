package estimations

class UniformParameters : ParameterEstimatorIfc{
    override fun estimate(data: DoubleArray): Result<DoubleArray>{

        //Calculate Values
        val alpha = data.minOrNull()
        val beta = data.maxOrNull()

        //Error Check
        return if(alpha == null || beta == null){
            estimateFailure("Provided data array is empty")
        } else {
            estimateSuccess(alpha, beta)
        }
    }
}