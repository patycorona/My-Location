package com.example.mylocation.ui.home.views

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mylocation.databinding.FragmentHomeBinding
import com.example.mylocation.domian.model.ConstantGeneral.Companion.LOTTIE_SAY_HI
import com.example.mylocation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    var binding: FragmentHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(context),null, false)

        listener()
        startLottieAnimation()
        return binding?.root
    }

    private fun listener(){
        binding?.btnChangeTheme?.setOnClickListener { (activity as MainActivity).chooseThemeDialog() }
    }

    private fun startLottieAnimation() {
        binding?.apply {
            animationStartLottie.setAnimation(LOTTIE_SAY_HI)
            animationStartLottie.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {

                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {

                }
            })
            animationStartLottie.playAnimation()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}