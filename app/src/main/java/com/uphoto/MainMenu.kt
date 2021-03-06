package com.uphoto

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView



/**
 * A simple [Fragment] subclass.
 * Use the [MainMenu.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "main_activity"
class MainMenu : Fragment() {


    private lateinit var normalEdit: Button
    private lateinit var weirdFilter: Button
    private lateinit var faceFilter: Button
    private lateinit var appname: ImageView

    private var dataPasser: OnDataPass? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "datapasser")
        dataPasser = context as OnDataPass
    }
    fun passData(data: String){
        dataPasser?.onDataPass(data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "bundle start")


    }
    fun onClick(view: String)
    {
      if(view.equals("normal edit"))
      {
          passData("select screen")
      }
      else if(view.equals("weird filter"))
      {
          passData("weird filter")
      }
      else if(view.equals("face filter"))
      {
          passData("face filter")
      }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_menu, container, false)
        normalEdit = view.findViewById(R.id.normal)
        appname = view.findViewById(R.id.name)
        weirdFilter = view.findViewById(R.id.filter)
        faceFilter = view.findViewById(R.id.face_filter)
        normalEdit.setOnClickListener{
            onClick("normal edit")
        }
        weirdFilter.setOnClickListener{
            onClick("weird filter")
        }
        faceFilter.setOnClickListener{
            onClick("face filter")
        }
        Log.d(TAG, "view create")

        return view
    }


    companion object {

        fun newInstance():MainMenu{
            return MainMenu()
        }
    }
    override fun onDetach() {
        super.onDetach()
        dataPasser = null
    }
}