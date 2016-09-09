package com.eleinco.ezvoxptt.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.eleinco.ezvoxptt.business.Equipos;
import com.eleinco.ezvoxptt.business.Grupos;
import com.eleinco.ezvoxptt.business.Login;
import com.eleinco.ezvoxptt.core.business.entities.ElementoTO;
import com.eleinco.ezvoxptt.entities.EquipoGrupo;
import com.eleinco.ezvoxptt.entities.Grupo;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

//actividad, interfaz para administrar los grupos
public class ActAdminGrupos extends Activity {
    //variables de interfaz de usuario
    private String TAG = "ActAdminGrupos";
    private Handler myHandler = new Handler();
    private ListView lstGrupos = null;
    private TextView lblNoHayGrupos = null;
    private AdpElementos adaptador = null;
    private ArrayList<ElementoTO> elementos = null;
    private Intent intAnadir = null;
    //private GrupoEquipo GrupoActual = null; //Queda null si es el administrador general, si es un administrador de un grupo queda el grupo en el que esta

    private String idGrupo = null;
    private String nombreGrupo = "";
    private String idDueno = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.act_admin_grupos);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.encabezado);

        elementos = new ArrayList<ElementoTO>();
        //llenar los elementos de interfaz
        ImageButton btnAction1 = (ImageButton) this.findViewById(R.id.btnAction1);
        TextView lblNombreApp = (TextView) this.findViewById(R.id.lblNombreApp);
        try {
            //obtener parametros de entrada
            Bundle pars = this.getIntent().getExtras();

            if (pars != null) {
                if (pars.containsKey("idGrupo"))
                    idGrupo = pars.getString("idGrupo");
                if (pars.containsKey("idDueno"))
                    idDueno = pars.getString("idDueno");
                if (pars.containsKey("nombreGrupo"))
                    nombreGrupo = pars.getString("nombreGrupo");
            }

            if (idGrupo == null && idDueno == null) {
                //Se solicita contraseña administradora y se cargan todos los grupos superiorres, Clietnes ELEINCO
                SolicitarLoginAdmin();
                //	lblNombreApp.setText("Administrador");
            } else {
                //GrupoActual = App.GrupoActual;
                lblNombreApp.setText("Grupo " + nombreGrupo);
                //Se debe cargar con el grupo Actual
                CargarElementos();
            }
            //llenar los elementos de interfaz
            lstGrupos = (ListView) findViewById(R.id.lstGrupos);
            lblNoHayGrupos = (TextView) findViewById(R.id.lblNoHayGrupos);
            btnAction1.setVisibility(View.VISIBLE);
            btnAction1.setOnClickListener(btnAnadirElemento_Click);
            lstGrupos.setOnItemClickListener(lstGrupos_ItemClick);
            lstGrupos.setOnItemLongClickListener(lstGrupos_ItemLongClick);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //evento del boton añadir elemento
    private OnClickListener btnAnadirElemento_Click = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (idDueno == null && idGrupo == null)
                AnadirGrupo(); //añadir un grupo en caso de que no haya dieño ni idgrupo padre (parte master)
            else {
                //preguntar por la entidad que se desea crear
                Confirm.Show("Que desea crear?",
                        "Equipo", "Grupo", ActAdminGrupos.this,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    AnadirEquipo();
                                } else {
                                    AnadirGrupo();
                                }
                            }
                        });
            }
        }
    };

    //mostrar interfaz para añadir equipo nuevo
    private void AnadirEquipo() {
        Confirm.Show("Que desea hacer?\n" +
                        "La opción 'Equipo Nuevo' le permite registrar un equipo nuevo.\n" +
                        "La opción 'Equipo existente' le permite añadir a este grupo un equipo que ya existe en otro grupo.",
                "Equipo Nuevo", "Equipo existente", ActAdminGrupos.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            intAnadir = new Intent(ActAdminGrupos.this, ActAnadirEquipo.class);
                            intAnadir.putExtra("idGrupo", idGrupo);
                            intAnadir.putExtra("idDueno", idDueno);
                            intAnadir.putExtra("tipoAccion", "AddNuevo");
                            startActivityForResult(intAnadir, 0);
                        } else {
                            SeleccionarElegirEquipo();
                        }
                    }
                });
    }

    //mostrar actividad de ingreso de grupos
    private void AnadirGrupo() {
        intAnadir = new Intent(ActAdminGrupos.this, ActAnadirGrupo.class);
        intAnadir.putExtra("IdGrupoPadre", idGrupo);
        intAnadir.putExtra("IdGrupoDueno", idDueno);
        startActivityForResult(intAnadir, 0);
    }

    //al hacer clic en el listview
    AdapterView.OnItemClickListener lstGrupos_ItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
            if (elementos != null) {
                //obtener el elemento de la posicion clickeada
                ElementoTO objElemento = elementos.get(pos);
                if (objElemento.grupo != null) //si es un grupo
                {
                    //abrir la informacion de ese grupo
                    intAnadir = new Intent(ActAdminGrupos.this, ActAdminGrupos.class);

                    intAnadir.putExtra("idGrupo", objElemento.grupo.ID_Grupo);
                    if (objElemento.grupo.ID_Grupo_Dueno == null || objElemento.grupo.ID_Grupo_Dueno.length() == 0)
                        intAnadir.putExtra("idDueno", objElemento.grupo.ID_Grupo);
                    else
                        intAnadir.putExtra("idDueno", objElemento.grupo.ID_Grupo_Dueno);
                    intAnadir.putExtra("nombreGrupo", objElemento.grupo.Nombre);
                    startActivityForResult(intAnadir, 0);
                } else {
                    //si es un equipo
                    final EquipoGrupo objEquipo = objElemento.equipo;
                    //pregutar si se desea cambiar el id del equipoa ctual por el seleccionado
                    Confirm.Show("Desea que este equipo sea '" + objEquipo.Nombre + "'?", "SI", "NO",
                            ActAdminGrupos.this, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == DialogInterface.BUTTON_POSITIVE) {
                                        Intent i = new Intent();
                                        i.putExtra("EquipoID", objEquipo.IDEquipo);
                                        setResult(2, i);
                                        ActAdminGrupos.this.finish();
                                    }
                                }
                            });
                }
            }
        }
    };
    //contextual de la lista de grupos
    OnItemLongClickListener lstGrupos_ItemLongClick = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
            if (elementos != null) {
                //obtener el elemento
                String nombreElemento = null;
                final ElementoTO objElemento = elementos.get(pos);
                if (objElemento.grupo != null)
                    nombreElemento = "grupo";
                else
                    nombreElemento = "equipo";
                //confirmar eliminacion
                Confirm.Show("Desea eliminar definitivamente este '" + nombreElemento + "'?", "SI", "NO",
                        ActAdminGrupos.this, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    //mostrar dialogo de carga
                                    Wait.Show("Eliminando...", ActAdminGrupos.this);
                                    //crear un hilo para no bloequar la interfaz
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // TODO Auto-generated method stub
                                            try {
                                                String nom = "";
                                                boolean eliminado = false;
                                                if (objElemento.grupo != null) {
                                                    nom = "grupo";
                                                    eliminado = Grupos.Eliminar(objElemento.ID); //eliminar el grupo
                                                } else {
                                                    nom = "equipo";
                                                    eliminado = Equipos.Eliminar(objElemento.ID); //eliminar el equipo
                                                }
                                                final String nombre = nom;
                                                if (eliminado) {
                                                    //salirse al contexto del UI
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            MsgBox.Show("El " + nombre + " ha sido eliminado correctamente.", ActAdminGrupos.this, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    CargarElementos();
                                                                }
                                                            });
                                                        }
                                                    });
                                                } else {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            MsgBox.Show("El " + nombre + " no ha sido eliminado.", ActAdminGrupos.this);
                                                        }
                                                    });
                                                }
                                            } catch (IOException e) {
                                                Error(e.getMessage(), false);
                                            } catch (XmlPullParserException e) {
                                                Error(e.getMessage(), false);
                                            }
                                        }
                                    }).start();
                                }
                            }
                        });

            }
            return true;
        }
    };

    //permite seleccionar un equipo de una lista
    private void SeleccionarElegirEquipo() {
        Wait.Show("Consultando lista de equipos...", ActAdminGrupos.this);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //cargar los equipos dependiendo del estado, si existe el id del dieño se consulta de ese id
                    //si no, se consultan todos. (master)
                    ArrayList<EquipoGrupo> equipos = null;
                    if (idDueno.length() == 0)
                        equipos = Equipos.Listar(null, true);
                    else equipos = Equipos.Listar(idDueno, true);
                    //cargar solo los nombres de los equipos en una lista
                    final ArrayList<String> nombres = new ArrayList<String>();
                    for (com.eleinco.ezvoxptt.entities.EquipoGrupo equipo : equipos) {
                        nombres.add(equipo.Nombre);
                    }
                    final ArrayList<EquipoGrupo> equiposAgregar = equipos;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Wait.Close();//cerrar carga
                            //abrir un dialogo con los equipo
                            AlertDialog.Builder alert = new AlertDialog.Builder(ActAdminGrupos.this);
                            alert.setTitle("Equipo");
                            alert.setMessage("Por favor seleccione el equipo:");
                            final Spinner cboDestino = new Spinner(ActAdminGrupos.this);
                            ArrayAdapter<String> aa = new ArrayAdapter<String>(
                                    ActAdminGrupos.this,
                                    android.R.layout.simple_spinner_item,
                                    nombres);

                            aa.setDropDownViewResource(
                                    android.R.layout.simple_spinner_dropdown_item);
                            cboDestino.setAdapter(aa);


                            alert.setView(cboDestino);
                            alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //abrir una actividad de ingreso de equipos con el equipo seleccioando
                                    int pos = cboDestino.getSelectedItemPosition();
                                    com.eleinco.ezvoxptt.entities.EquipoGrupo equipoSeleccionado =
                                            equiposAgregar.get(pos);
                                    Intent anadirEquipoIntent = new Intent(ActAdminGrupos.this, ActAnadirEquipo.class);
                                    anadirEquipoIntent.putExtra("idGrupo", idGrupo);
                                    anadirEquipoIntent.putExtra("idDueno", idDueno);
                                    anadirEquipoIntent.putExtra("idEquipo", equipoSeleccionado.IDEquipo);
                                    anadirEquipoIntent.putExtra("nombreEquipo", equipoSeleccionado.Nombre);
                                    anadirEquipoIntent.putExtra("tipoAccion", "AddExistente");

                                    startActivityForResult(anadirEquipoIntent, 0);
                                }
                            });
                            alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            });
                            alert.show();

                        }
                    });

                } catch (final Exception ex) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Wait.Close();
                            MsgBox.Show(ex.getMessage(), ActAdminGrupos.this);
                        }
                    });
                }
            }
        }).start();

    }

    //sollicita la contraseña master
    private void SolicitarLoginAdmin() {
        //Solicitar la clave para poder acceder
        AlertDialog.Builder alert = new AlertDialog.Builder(ActAdminGrupos.this);
        alert.setTitle("Administrador");
        alert.setMessage("Clave:");
        final EditText input = new EditText(ActAdminGrupos.this);
        //input.setHint("Clave");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alert.setView(input);
        alert.setCancelable(false);
        alert.setPositiveButton("Ingresa", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String clave = input.getText().toString();
                Wait.Show("Verificando...", ActAdminGrupos.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //validar la informacion
                            final boolean logueado = Login.LoginMaster(clave);
                            if (logueado == false) {
                                Error("Clave invalida, por favor verifique", true);
                                return;
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Wait.Close();
                                    idGrupo = null; //Porque es administrador
                                    CargarElementos();
                                }
                            });
                        } catch (IOException e) {
                            Error("Error al conectarse, por favor intente de nuevo.", true);
                        } catch (XmlPullParserException e) {
                            Error("Error de respuesta, por favor intente de nuevo.", true);
                        }
                    }
                }).start();
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ActAdminGrupos.this.finish();
            }
        });
        alert.show();
        /*(new Handler()).postDelayed(new Runnable() {
            public void run() {
               input.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
               input.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
            }
        }, 100);*/
    }

    //muestra un error saliento a la UI
    private void Error(final String mensaje, final boolean cerrarActividad) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Wait.Close();
                MsgBox.Show(mensaje, ActAdminGrupos.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (cerrarActividad)
                            ActAdminGrupos.this.finish();
                    }
                });
            }
        });
    }

    //carga un elemento (grupo o equiop)
    private void CargarElementos() {
        Wait.Show("Se están cargando los datos...", this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Se consultan los grupos
                try {
                    if (elementos != null)
                        elementos.clear();
                    ArrayList<Grupo> grupos = Grupos.Listar(idGrupo);

                    ArrayList<EquipoGrupo> equipos = null;
                    if (idGrupo != null) {
                        boolean cargarSiDueno = (idDueno == null);
                        equipos = Equipos.Listar(idGrupo, cargarSiDueno);
                    } else equipos = Equipos.Listar(null, false);

                    for (Grupo g : grupos) {
                        elementos.add(new ElementoTO(g));
                    }
                    for (EquipoGrupo e : equipos) {
                        elementos.add(new ElementoTO(e));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Wait.Close();
                            try {
                                adaptador = new AdpElementos(ActAdminGrupos.this, elementos);
                                lstGrupos.setAdapter(adaptador);
                                if (elementos.size() > 0)
                                    lblNoHayGrupos.setVisibility(View.GONE);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Error(ex.getMessage(), true);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Error(e.getMessage(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                    Error(e.getMessage(), true);
                }
            }
        }).start();
    }

    //cuando seseleccionaun item del menu android
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_configuraciones) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) //Anadir grupo
        {
            if (resultCode == 2) {
                setResult(2, data);
                ActAdminGrupos.this.finish();
            } else if (resultCode == RESULT_OK) //Hubieron cambios
            {
                setResult(RESULT_OK);
                CargarElementos();
            }
        }
    }
}
