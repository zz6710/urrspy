package com.kayak.dps.valtabimp;

import com.kayak.dps.valtabimp.model.Vwsysfieldcfg;
import com.kayak.dps.valtabimp.repository.ValFunctionFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Share_Expr {
	private String doubleReg = "^[-//+]?\\d+\\.\\d+";//"^[-\\+]?[.\\d]*$"
	private String intReg = "^[0-9]*$";
	private String letterReg = "^[A-Za-z]+$";
	private ValFunctionFactory functionfactory=null;

	@SuppressWarnings("rawtypes")
	public Map configFieldnames = new HashMap();

	public String key;


	public class TEvaluator {
		private TEvResult EvalConstant(String strConstant) {
			TEvResult result = new TEvResult();
			int N = 0;
			String aString;

			aString = strConstant;
			while ( N < aString.length() && String.valueOf(aString.charAt(N)).matches(intReg)) {//0-9
				N++;
			}
			result.Kind = TEvResultType.resInt;
			while ( N < aString.length() && String.valueOf(aString).matches(doubleReg)){//0-9 .eE+-
				N++;
				result.Kind = TEvResultType.resDouble;
			}
			if( N != aString.length()) {
				result = ErrorCreate(String.format(SqrExpIllegalCharInNumeric, aString));
			} else {
				if(result.Kind == TEvResultType.resInt) {
					try {
						result.intResult = Integer.parseInt(aString);
					} catch (Exception ex) {
						result.Kind = TEvResultType.resDouble;
					}
				}
				if (result.Kind == TEvResultType.resDouble) {
					try {
						result.dblResult = Double.parseDouble(aString);
					} catch (Exception ex) {
						result = ErrorCreate(String.format(SqrExpIllegalCharInNumeric, aString));
					}
				}
			}
			if (result.Kind == TEvResultType.resError) {
				FiFo.Put(new TEvElementError(result.strResult));
			} else {
				FiFo.Put(new TEvElementConstant(result));
			}
			return result;
		}

		@SuppressWarnings("unused")
		private TEvResult EvalEnvironment(String strVariable) {
			TEvResult result = null;
			TEvElement AElement;

			return result;
		}

		private TEvResult EvalFactor(String strFactorExpr) {
			TEvResult result = new TEvResult();
			String aString;
			TEvResult aResult;
			//TrimString(strFactorExpr);
			strFactorExpr = strFactorExpr.trim();

			String strSub = strFactorExpr;
			if (strFactorExpr.length() >= 3) {
				strSub = strFactorExpr.substring(0,3);
			}
			if ("not".equalsIgnoreCase(strSub)) {
				strFactorExpr = '(' + "true xor " + strFactorExpr.substring(3, strFactorExpr.length()) + ")";
			}
			aString = strFactorExpr;

			if ("not".equalsIgnoreCase(strSub)) {
				aResult = EvalSimpleExpr(strFactorExpr.substring(3, strFactorExpr.length()));
				if (aResult.Kind == TEvResultType.resBool) {
					result.booResult = (!aResult.booResult);
					result.Kind = aResult.Kind;
				} else {
					result = ErrorCreate(SqrInvalidNot);
				}
			} else {
				String strVal = Character.toString(aString.charAt(0));
				if (strVal.matches(letterReg)) {//A-Z a-z
					result = EvalFunctionExpr(strFactorExpr);
				} else if (strVal.matches(intReg)) {//0-9
					result = EvalConstant(strFactorExpr);
				} else if ("-".equals(strVal)) {
					result = EvalSimpleExpr("0-" + strFactorExpr.substring(1, strFactorExpr.length()));
				} else if ("+".equals(strVal)) {
					result = EvalFactor(strFactorExpr.substring(1, strFactorExpr.length()));
				} else if ("(".equals(strVal)) {
					if (")".equals(Character.toString(strFactorExpr.charAt(strFactorExpr.length() - 1)))) {
						result = Evaluate(strFactorExpr.substring(1, strFactorExpr.length() - 1));
					} else {
						result = ErrorCreate(String.format(SqrExpMissing, ")"));
						FiFo.Put(new TEvElementError(result.strResult));
					}
				} else if ("'".equals(strVal)) {
					if("'".equals(Character.toString(strFactorExpr.charAt(strFactorExpr.length() - 1)))) {
						result = EvalString(strFactorExpr.substring(1, strFactorExpr.length() - 1));
					} else {
						result = ErrorCreate(String.format(SqrExpMissing, ")"));
						FiFo.Put(new TEvElementError(result.strResult));
					}
				} else if ("[".equals(strVal)) {
					if ("]".equals(Character.toString(strFactorExpr.charAt(strFactorExpr.length() - 1)))) {
						result = EvalVariable(strFactorExpr.substring(1, strFactorExpr.length() - 1));
					} else {
						result = ErrorCreate(String.format(SqrExpMissing, "]"));
						FiFo.Put(new TEvElementError(result.strResult));
					}
				} else {
					result = EvalFunctionExpr(strFactorExpr);
				}
			}
			return result;
		}

		private TEvResult EvalFunctionExpr(String strFunc) {
			TEvResult result = new TEvResult();
			TEvResult argRes = new TEvResult();
			int po;
			po = strFunc.indexOf("(");
			if (po > 0) {
				if (strFunc.substring(strFunc.length() - 1, strFunc.length()).equals(")")) {
					result = EvalFunction(strFunc.substring(0, po), strFunc.substring(po + 1, strFunc.length() - 1));
				} else {
					result = EvalFunction("", "");
				}
			} else {
				argRes.Kind = TEvResultType.resError;
				result = EvalFunction(strFunc, "");
			}
			return result;
		}

		private TEvResult EvalSimpleExpr(String strSimplExpr) {
			TEvResult result = null;
			TEvOperator Op = null;
			int intStart,intLen = 1;
			@SuppressWarnings("unused")
			TEvResult Res1,Res2;
			int n = 0;
			int intParenteses = 0;
			boolean booFound = false;
			boolean booString = false;
			boolean booBracket = false;

			String strVal = "";
			while (n < strSimplExpr.length() && !booFound) {
				booFound = true;
				strVal = Character.toString(strSimplExpr.charAt(n));
				if ("(".equals(strVal)) {
					if (!(booString || booBracket)) intParenteses++;
				} else if(")".equals(strVal)) {
					if (!(booString || booBracket)) intParenteses--;
				} else if("[".equals(strVal)) {
					if (!(booString || booBracket)) booBracket = true;
				} else if("]".equals(strVal)) {
					if ((!booString) && booBracket) booBracket = false;
				} else if("'".equals(strVal)) {
					if (!booBracket) {
						if (booString) booString = false;
						else booString = true;
					}
				}
				if ((intParenteses == 0) && (n > 0) && !(booString || booBracket)) {
					if ("+".equals(strVal)) {
						Op = TEvOperator.opPlus;
					} else if ("-".equals(strVal)) {
						Op = TEvOperator.opMinus;
					} else if (" ".equals(strVal)) {
						if ("or ".equalsIgnoreCase(strSimplExpr.substring(n + 1, n + 4))) {
							Op = TEvOperator.opOr;
							intLen = 4;
							//n++;
						} else {
							booFound = false;
						}
					} else {
						booFound = false;
					}
				} else {
					booFound = false;
				}
				n++;
			}

			if (booFound) intStart = n - 1;
			else intStart = -1;

			if (intStart > 0) {
				FiFo.Put(new TEvElementOperator(Op));
				Res1 = EvalTerm(strSimplExpr.substring(0, intStart));
				if (Op == TEvOperator.opMinus) {
					Res2 = EvalSimpleExpr(Flip(strSimplExpr.substring(intStart + intLen, strSimplExpr.length()), '+', '-'));
				} else {
					Res2 = EvalSimpleExpr(strSimplExpr.substring(intStart + intLen, strSimplExpr.length()));
				}
			} else {
				result = EvalTerm(strSimplExpr);
			}

			return result;
		}

		private TEvResult EvalString(String strString) {
			TEvResult result = new TEvResult();
			result.Kind = TEvResultType.resString;
			result.strResult = strString;
			FiFo.Put(new TEvElementString(result.strResult));
			return result;
		}

		private TEvResult EvalTerm(String strTermExpr) {
			TEvResult result = null;
			TEvOperator Op = null;
			int intStart,intLen = 1;
			@SuppressWarnings("unused")
			TEvResult Res1,Res2;
			int n = 0;
			boolean booFound = false;
			boolean booString = false;
			boolean booBracket = false;
			int intParenteses = 0;

			String strVal = "";
			while (n < strTermExpr.length() && !booFound) {
				booFound = true;
				strVal = Character.toString(strTermExpr.charAt(n));
				if ("(".equals(strVal)) {
					if (!(booString || booBracket)) intParenteses++;
				} else if(")".equals(strVal)) {
					if (!(booString || booBracket)) intParenteses--;
				} else if("[".equals(strVal)) {
					if (!(booString || booBracket)) booBracket = true;
				} else if("]".equals(strVal)) {
					if ((!booString) && booBracket) booBracket = false;
				} else if("'".equals(strVal)) {
					if (!booBracket) {
						if(booString) booString = false;
						else booString = true;
					}
				}
				if ((intParenteses == 0) && (n > 0) && !(booString || booBracket)) {
					if ("*".equals(strVal)) {
						Op = TEvOperator.opMul;
					} else if ("/".equals(strVal)) {
						Op = TEvOperator.opDiv;
					} else if (" ".equals(strVal)) {
						if("and ".equalsIgnoreCase(strTermExpr.substring(n + 1, n + 5))) {
							Op = TEvOperator.opAnd;
							intLen = 5;
							//n++;
						} else {
							booFound = false;
						}
					} else {
						booFound = false;
					}
				} else {
					booFound = false;
				}
				n++;
			}

			if (booFound) intStart = n - 1;
			else intStart = -1;

			if (intStart > 0) {
				FiFo.Put(new TEvElementOperator(Op));
				Res1 = EvalFactor(strTermExpr.substring(0, intStart));
				if (Op == TEvOperator.opDiv) {
					Res2 = EvalTerm(Flip(strTermExpr.substring(intStart + intLen, strTermExpr.length()), '*', '/'));
				} else {
					Res2 = EvalTerm(strTermExpr.substring(intStart + intLen, strTermExpr.length()));
				}
			} else {
				result = EvalFactor(strTermExpr);
			}

			return result;
		}

		private TEvResult Evaluate(String strExpr) {
			TEvResult result = null;
			int n = 0;
			boolean booFound = false;
			int intParenteses = 0;
			boolean booString = false;
			boolean booBracket = false;
			TEvOperator Op;
			int intStart,intLen = 1;
			@SuppressWarnings("unused")
			TEvResult Res1 = null,Res2 = null;

			Op = TEvOperator.opEqual;
			String strVal = "";
			while (n < strExpr.length() && !booFound) {
				booFound = true;
				strVal = Character.toString(strExpr.charAt(n));
				if ("(".equals(strVal)) {
					if (!(booString || booBracket)) intParenteses++;
				} else if (")".equals(strVal)) {
					if (!(booString || booBracket)) intParenteses--;
				} else if ("[".equals(strVal)) {
					if(!(booString || booBracket)) booBracket = true;
				} else if ("]".equals(strVal)) {
					if ((!booString) && booBracket) booBracket = false;
				} else if ("'".equals(strVal)) {
					if (!booBracket) {
						if(booString) booString = false;
						else booString = true;
					}
				}
				if ((intParenteses == 0) && (n > 0) && !(booString || booBracket)) {
					if ("<".equals(strVal)) {
						if (">".equals(Character.toString(strExpr.charAt(n + 1)))) {
							Op = TEvOperator.opUnequal;
							intLen = 2;
						} else if ("=".equals(Character.toString(strExpr.charAt(n + 1)))) {
							Op = TEvOperator.opLessOrEqual;
							intLen = 2;
						} else {
							Op = TEvOperator.opLess;
						}
					} else if (">".equals(strVal)) {
						if ("=".equals(Character.toString(strExpr.charAt(n + 1)))) {
							Op = TEvOperator.opGreaterOrEqual;
							intLen = 2;
						} else {
							Op = TEvOperator.opGreater;
						}
					} else if ("=".equals(strVal)) {
						Op = TEvOperator.opEqual;
					} else {
						booFound = false;
					}
				} else {
					booFound = false;
				}
				n++;
			}

			if (booFound) intStart = n - 1;
			else intStart = -1;

			if (intStart > 0) {
				FiFo.Put(new TEvElementOperator(Op));
				Res1 = EvalSimpleExpr(strExpr.substring(0, intStart));
				Res2 = EvalSimpleExpr(strExpr.substring(intStart + intLen, strExpr.length()));
			} else {
				result = EvalXorExpr(strExpr);
			}
			return result;
		}

		private TEvResult EvalXorExpr(String strExpr) {
			TEvResult result = null;
			int n = 0;
			boolean booFound = false;
			int intParenteses = 0;
			boolean booString = false;
			boolean booBracket = false;
			TEvOperator Op;
			int intStart,intLen = 1;
			@SuppressWarnings("unused")
			TEvResult Res1,Res2;

			Op = TEvOperator.opEqual;
			String strVal = "";
			while (n < strExpr.length() && !booFound) {
				booFound = true;
				strVal = Character.toString(strExpr.charAt(n));
				if ("(".equals(strVal)) {
					if (!(booString || booBracket)) intParenteses++;
				} else if (")".equals(strVal)) {
					if (!(booString || booBracket)) intParenteses--;
				} else if ("[".equals(strVal)) {
					if (!(booString || booBracket)) booBracket = true;
				} else if ("]".equals(strVal)) {
					if ((!booString) && booBracket) booBracket = false;
				} else if ("'".equals(strVal)) {
					if (!booBracket) {
						if (booString) booString = false;
						else booString = true;
					}
				}
				if ((intParenteses == 0) && (n > 0) && !(booString || booBracket)) {
					if (" ".equals(strVal)) {
						if ("xor ".equalsIgnoreCase(strExpr.substring(n + 1, n + 5))) {
							Op = TEvOperator.opXor;
							intLen = 4;
							n++;
						} else {
							booFound = false;
						}
					} else {
						booFound = false;
					}
				} else {
					booFound = false;
				}
				n++;
			}

			if (booFound) intStart = n - 1;
			else intStart = -1;

			if (intStart > 0) {
				FiFo.Put(new TEvElementOperator(Op));
				Res1 = EvalSimpleExpr(strExpr.substring(0, intStart - 1));
				Res2 = EvalSimpleExpr(strExpr.substring(intStart + intLen, strExpr.length()));
			} else {
				result = EvalSimpleExpr(strExpr);
			}
			return result;
		}

		@SuppressWarnings("rawtypes")
		private	HashMap FDataSets;
		@SuppressWarnings("unused")
		private TEvEnvironment FEnvironment;
		private TFiFo FiFo;

		private int FindDelimiter(String strArg, int Pos) {
			int n = 0;
			boolean FoundDelim = false;
			boolean booString = false;
			int intParenteses = 0;
			if ("".equals(strArg)) {
				Pos = 0;
			} else {
				while (n < strArg.length() && !FoundDelim) {
					String strVal = Character.toString(strArg.charAt(n));
					if ("(".equals(strVal)) {
						if (!booString) intParenteses++;
					} else if (")".equals(strVal)) {
						if (!booString) intParenteses--;
					} else if ("'".equals(strVal)) {
						if (booString) booString = false;
						else booString = true;
					}
					if (intParenteses == 0 && !booString) {
						if (ArgSeparator.equals(strVal)) {
							FoundDelim = true;
							break;
						}
					}
					n++;
				}
				if (FoundDelim) {
					Pos = n;
				} else {
					Pos = 0;
				}
			}
			return Pos;
		}

		private boolean FPrepared;
		public boolean isFPrepared() {
			return FPrepared;
		}
		public void setFPrepared(boolean fPrepared) {
			FPrepared = fPrepared;
		}

		@SuppressWarnings("unused")
		private boolean GetAggregate() {
			return FiFo.isFAggreg();
		}

		@SuppressWarnings("rawtypes")
		private HashMap OwnDataSets;

		private void SetAggregate(boolean Value) {
			FiFo.setFAggreg(Value);
		}

		@SuppressWarnings("unused")
		private void TrimString() {

		}

		protected TEvResult EvalFunction(String strFunc, String strArg) {
			int DelimPos = 0;
			String aString;
			TEvResult Res = null;
			TEvElement aFunc;

			strFunc = strFunc.toUpperCase();
			aFunc = FunctionLibrary.GetFunction(strFunc);
			if (aFunc instanceof TEvElementError) {
				if ("".equals(strArg)) {
					//AFunc.Free;
					Res = EvalVariable(strFunc);
				} else {
					FiFo.Put(aFunc);
				}
			} else {
				FiFo.Put(aFunc);
				if (!(aFunc instanceof TEvElementError)) {
					aString = strArg;
					do {
						DelimPos = FindDelimiter(aString, DelimPos);
						if (DelimPos > 0) {
							Res = Evaluate(aString.substring(0, DelimPos));
						} else {
							if (aString.length() > 0)  Res = Evaluate(aString);
						}
						aString = aString.substring(DelimPos+1, aString.length());
					} while (DelimPos != 0);
				}
				FiFo.Put(new TEvElementArgumentEnd());
			}
			return Res;
		}

		protected TEvResult EvalVariable(String strVariable) {
			TEvResult result = null;

			if (FDataSets.size() > 0) {
				if (FDataSets.containsKey(strVariable)) {
					result = new TEvResult();
					Object value = FDataSets.get(strVariable);
					if (value instanceof Double || value instanceof BigDecimal) {
						result.dblResult = Double.parseDouble(value.toString());
						result.Kind = TEvResultType.resDouble;
					} else if (value instanceof Integer || value instanceof Long) {
						result.intResult = Integer.parseInt(value.toString());
						result.Kind = TEvResultType.resInt;
					} else {
						result.strResult = value.toString();
						result.Kind = TEvResultType.resString;
					}

					FiFo.Put(new TEvElementField(result,strVariable));
				} else {
					//EvalEnvironment(strVariable);
				}
			} else {
				//EvalEnvironment(strVariable);
			}
			return result;
		}

		@SuppressWarnings("rawtypes")
		protected HashMap GetDatasets() {
			if (FDataSets == null) {
				FDataSets = new HashMap();
				OwnDataSets = FDataSets;
			}
			return FDataSets;
		}

		protected boolean GetIsAggreg() {
			boolean result = false;
			for (int i = 0; i < FiFo.FiFo.size(); i++) {
				result = result || ((TEvElement)FiFo.FiFo.get(i)).isFIsAggreg();
			}
			return result;
		}

		@SuppressWarnings("rawtypes")
		protected void SetDatasets(HashMap Value) {
			if (FDataSets != null && FDataSets != Value && FDataSets != OwnDataSets) {
				FDataSets.clear();
			}
			FDataSets = Value;
		}

		public boolean AsBoolean() {
			boolean result = false;
			TEvResult AValue;
			AValue = Value();
			if (AValue.Kind == TEvResultType.resBool) {
				result = AValue.booResult;
			} else if (AValue.Kind == TEvResultType.resString) {
				if (AValue.strResult.equalsIgnoreCase("TRUE")) {
					result = true;
				} else if (AValue.strResult.equalsIgnoreCase("FALSE")) {
					result = false;
				} else {
					new Exception(String.format(SqrNotValue, SqrExpBoolean));
				}
			} else {
				new Exception(String.format(SqrNotValue, SqrExpBoolean));
			}
			return result;
		}

		public TEvResult Calculate(String strExpr) {
			TEvResult result = null;
			LogStr = strExpr + "\r\n";
			Prepare(strExpr);
			result = Value();
			//UnPrepare;
			return result;
		}

		public TEvResult CalculateAndLog(String strExpr) {
			TEvResult result = null;
			try {
				//LogStr = strExpr + "\r\n";
				Prepare(strExpr);
				result = Value();
				if (result.Kind == TEvResultType.resInt) {
					LogStr = (LogStr==null?"":LogStr + "      ") + "计算结果:" + String.valueOf((result.intResult));
				} else if (result.Kind == TEvResultType.resDouble) {
					if(configFieldnames.containsKey(key)) {
						Vwsysfieldcfg expressionconfigs = (Vwsysfieldcfg) configFieldnames.get(key);
						String datafmt = expressionconfigs.getData_fmt() == null ? "" : expressionconfigs.getData_fmt();
						if(!"".equals(datafmt.trim())) {
							String scale = datafmt.split("\\.", -1)[1];
							if(scale != null) {
								BigDecimal b = BigDecimal.valueOf(result.dblResult);
								String TextResult = b.setScale(scale.length(), BigDecimal.ROUND_HALF_UP).toString();
								LogStr = (LogStr==null?"":LogStr + "      ") + "计算结果:" + TextResult;
							} else {
								DecimalFormat df = new DecimalFormat(datafmt);
								LogStr = (LogStr==null?"":LogStr + "      ") + "计算结果:" + df.format(result.dblResult);
							}
						} else {
							BigDecimal b =  BigDecimal.valueOf(result.dblResult);
							String TextResult = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
							LogStr = (LogStr==null?"":LogStr + "      ") + "计算结果:" + TextResult;
						}
					} else {
						BigDecimal b =  BigDecimal.valueOf(result.dblResult);
						String TextResult = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
						LogStr = (LogStr==null?"":LogStr + "      ") + "计算结果:" + TextResult;
					}
				} else if (result.Kind == TEvResultType.resString) {
					LogStr = (LogStr==null?"":LogStr + "      ") + "计算结果:" + result.strResult;
				} else if (result.Kind == TEvResultType.resBool) {
					if (result.booResult) {
						LogStr = (LogStr==null?"":LogStr + "      ") + "计算结果:true";
					} else {
						LogStr = (LogStr==null?"":LogStr + "      ") + "计算结果:false";
					}
				} else if (result.Kind == TEvResultType.resObject) {
					LogStr = (LogStr==null?"":LogStr + "      ") + "计算结果:对象";
				} if (result.Kind == TEvResultType.resError) {
					LogStr = (LogStr==null?"":LogStr + "      ") + "计算结果:出错";
				}
				CalcLogStr = LogStr;
				//UnPrepare;
			} catch(Exception ex) {
				CalcLogStr = LogStr + ex.getMessage();
			}
			return result;
		}

		public TEvaluator() {
			FPrepared = false;
			FEnvironment = null;
			FDataSets = null;
			OwnDataSets = null;
		}

		public void DoAggregate() {
			SetAggregate(true);
			Value();
			SetAggregate(false);
		}

		public void Prepare(String strExpr) {
			@SuppressWarnings("unused")
			TEvResult Value;
			if (isFPrepared()) {
				UnPrepare();
			}
			//GlobalEnvironment.Prepare;
			FiFo = new TFiFo();
			if ("".equals(strExpr))
				Value = Evaluate(" ");
			else
				Value = Evaluate(strExpr);
			FPrepared = true;
		}

		public void UnPrepare() {
			//GlobalEnvironment.Unprepare;
			FPrepared = false;
		}

		public TEvResult Value() {
			TEvResult result = new TEvResult();
			Object F;
			if (!isFPrepared()) {
				new Exception(SqrEvalNotPrepared);
			}
			FiFo.Start();
			F = FiFo.Get();
			if (F == null)
				result.setKind(TEvResultType.resError);
			else {
				result = ((TEvElement)F).Value(FiFo);
			}
			return result;
		}

	}

	public class TEvElement {
		private boolean FIsAggreg;
		public boolean isFIsAggreg() {
			return FIsAggreg;
		}
		public void setFIsAggreg(boolean fIsAggreg) {
			FIsAggreg = fIsAggreg;
		}

		public String CalcLog;

		public TEvElement() {
			FIsAggreg = false;
			CalcLog = "";
		}
		public void Reset(){}

		public TEvResult Value(TFiFo FiFo) {
			return null;
		}
	}

	public class TEvElementArgumentEnd extends TEvElement {

	}

	public class TEvElementConstant extends TEvElement {
		private TEvResult FValue;
		public TEvElementConstant(TEvResult Value) {
			FValue = Value;
		}
		public TEvResult Value(TFiFo FiFo) {
			return FValue;
		}
	}

	public class TEvElementField extends TEvElement {
		private TEvResult FValue;
		private String FFieldName;
		public TEvElementField(TEvResult Value,String FieldName) {
			FValue = Value;
			FFieldName = FieldName;
		}
		public TEvResult Value(TFiFo FiFo) {
			TEvResult result;
			result = FValue;
			if (result.Kind == TEvResultType.resInt) {
				CalcLog = (CalcLog==""?"": CalcLog + "      ") + FFieldName + ":" + String.valueOf(result.intResult);
			} else if (result.Kind == TEvResultType.resDouble) {
				String fieldName = FFieldName;
				if(fieldName.length() > 2) {
					if("期初".equals(fieldName.substring(0, 2))) {
						fieldName = fieldName.substring(2, fieldName.length());
					}
				}
				if(configFieldnames.containsKey(fieldName)) {
					Vwsysfieldcfg expressionconfigs = (Vwsysfieldcfg) configFieldnames.get(fieldName);
					String datafmt = expressionconfigs.getData_fmt()==null?"":expressionconfigs.getData_fmt();
					if(!"".equals(datafmt.trim())) {
						DecimalFormat df = new DecimalFormat(datafmt);
						CalcLog = (CalcLog==""?"": CalcLog + "      ") + FFieldName + ":" + df.format(result.dblResult);
					} else {
						CalcLog = (CalcLog==""?"": CalcLog + "      ") + FFieldName + ":" + result.dblResult;
					}
				} else {
					CalcLog = (CalcLog==""?"": CalcLog + "      ") + FFieldName + ":" + result.dblResult;
				}
			} else if (result.Kind == TEvResultType.resString) {
				CalcLog = (CalcLog==""?"": CalcLog + "      ") + FFieldName + ":" + result.strResult;
			} else if (result.Kind == TEvResultType.resBool) {
				if (result.booResult) {
					CalcLog = (CalcLog==""?"": CalcLog + "      ") + FFieldName + ":true";
				} else {
					CalcLog = (CalcLog==""?"": CalcLog + "      ") + FFieldName + ":false";
				}
			} else if (result.Kind == TEvResultType.resObject) {
				CalcLog = (CalcLog==""?"": CalcLog + "      ") + FFieldName + ":对象";
			} else if (result.Kind == TEvResultType.resError) {
				CalcLog = (CalcLog==""?"": CalcLog + "      ") + FFieldName + ":出错";
			}
			LogStr = (LogStr==null?"":LogStr+"      ") + CalcLog + "\r\n";

			return result;
		}
	}

	public class TEvElementError extends TEvElement {
		private String FErrorMessage;
		public TEvElementError(String ErrorMessage) {
			FErrorMessage = ErrorMessage;
		}
		public TEvResult Value(TFiFo FiFo) {
			TEvResult result = new TEvResult();
			result.Kind = TEvResultType.resError;
			result.strResult = FErrorMessage;
			return result;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public class TEvElementFunction extends TEvElement {
		protected void Aggregate() {}
		protected List ArgList;
		public String ArgString;

		public TEvElementFunction() {
			ArgList = new ArrayList();
			ArgString = "";
		}

		protected TEvResult Argument(int Index) {
			TEvResult result = null;
			if (Index < ArgList.size()) {
				result = ((TEvResultClass)ArgList.get(Index)).EvResult;
			}
			return result;
		}

		protected boolean ArgumentOK(TEvElement Value) {
			return !(Value instanceof TEvElementArgumentEnd) && !(Value instanceof TEvElementError);
		}

		protected TEvResult Calculate(){
			TEvResult result = new TEvResult();
			result.Kind = TEvResultType.resError;
			return result;
		}

		protected void FreeArguments(){
			ArgList.clear();
		}

		protected void GetArguments(TFiFo FiFo) {
			TEvElement aArgument = null;
			TEvResultClass aResult;
			do {
				aArgument = (TEvElement)FiFo.Get();
				if (!(aArgument instanceof TEvElementArgumentEnd)) {
					aResult = new TEvResultClass();
					aResult.EvResult = aArgument.Value(FiFo);
					ArgList.add(aResult);
					if (aResult.EvResult.Kind == TEvResultType.resDouble) {
						ArgString = ArgString + (ArgString.equals("")?"":";") + aResult.EvResult.dblResult;//df.format()
					} else if (aResult.EvResult.Kind == TEvResultType.resInt) {
						ArgString = ArgString + (ArgString.equals("")?"":";") + String.valueOf(aResult.EvResult.intResult);
					} else if (aResult.EvResult.Kind == TEvResultType.resString) {
						ArgString = ArgString + (ArgString.equals("")?"":";") + aResult.EvResult.strResult;
					} else if (aResult.EvResult.Kind == TEvResultType.resBool) {
						if (aResult.EvResult.booResult) {
							ArgString = ArgString + ";true";
						} else {
							ArgString = ArgString + ";false";
						}
					}
				}
			} while (!(aArgument instanceof TEvElementArgumentEnd));
		}

		public String FunctionArguments() {
			return "";
		}

		public String FunctionDescription() {
			return FunctionName();
		}

		public String FunctionName() {
			return "";
		}

		public String FunctionVendor() {
			return SqrQuSoft;
		}

		public TEvResult Value(TFiFo FiFo) {
			TEvResult result = null;
			GetArguments(FiFo);
			if (FiFo.isFAggreg()) {
				Aggregate();
			}
			result = Calculate();
			FreeArguments();
			if (result.Kind == TEvResultType.resInt) {
				CalcLog = CalcLog + "      " + FunctionName() + '(' + ArgString + ')' + ':' + String.valueOf(result.intResult);
			} else if (result.Kind == TEvResultType.resDouble) {
				CalcLog = CalcLog + "      " + FunctionName() + '(' + ArgString + ')' + ':' + result.dblResult;//df.format()
			} else if (result.Kind == TEvResultType.resString) {
				CalcLog = CalcLog + "      " + FunctionName() + '(' + ArgString + ')' + ':' + result.strResult;
			} else if (result.Kind == TEvResultType.resBool) {
				if (result.booResult) {
					CalcLog = CalcLog + "      " + FunctionName() + '(' + ArgString + ')' + ":true";
				} else {
					CalcLog = CalcLog + "      " + FunctionName() + '(' + ArgString + ')' + ":false";
				}
			} else if (result.Kind == TEvResultType.resObject) {
				CalcLog = CalcLog + "      " + FunctionName() + '(' + ArgString + ')' + ":对象";
			} else if (result.Kind == TEvResultType.resError) {
				CalcLog = CalcLog + "      " + FunctionName() + '(' + ArgString + ')' + ":出错";
			}
			LogStr = LogStr + CalcLog + "\r\n";
			return result;
		}
	}

	public class TEvElementOperator extends TEvElement {
		private TEvOperator FOpCode;

		public TEvElementOperator(TEvOperator OpCode){
			FOpCode = OpCode;
		}
		private void ConverTEvResults(TEvResult Res1,TEvResult Res2) {
			if(Res1.Kind != TEvResultType.resError && Res2.Kind != TEvResultType.resError) {
				if(Res1.Kind != Res2.Kind) {
					if(Res1.Kind == TEvResultType.resInt && Res2.Kind == TEvResultType.resDouble) {
						Res1.setKind(TEvResultType.resDouble);
						Res1.setDblResult(Res1.intResult);
					} else {
						if(Res1.Kind == TEvResultType.resDouble && Res2.Kind == TEvResultType.resInt) {
							Res2.setKind(TEvResultType.resDouble);
							Res2.setDblResult(Res2.intResult);
						} else {
							Res1.setStrResult(EvResultToString(Res1));
							Res1.setKind(TEvResultType.resString);
							Res2.setStrResult(EvResultToString(Res2));
							Res2.setKind(TEvResultType.resString);
						}
					}
				}
			}
		}

		public TEvResult Value(TFiFo FiFo) {
			TEvResult result = new TEvResult();
			TEvResult Res1,Res2;
			Res1 = ((TEvElement)FiFo.Get()).Value(FiFo);
			Res2 = ((TEvElement)FiFo.Get()).Value(FiFo);
			ConverTEvResults(Res1, Res2);
			result.Kind = Res1.Kind;
			if (Res2.Kind == TEvResultType.resError) {
				result.Kind = Res2.Kind;
			}
			if (result.Kind != TEvResultType.resError) {
				if (FOpCode == TEvOperator.opPlus) {
					if (result.Kind == TEvResultType.resInt) {
						result.intResult = Res1.intResult + Res2.intResult;
					} else if (result.Kind == TEvResultType.resDouble){
						result.dblResult = BigDecimal.valueOf(Res1.dblResult).add(BigDecimal.valueOf(Res2.dblResult)).doubleValue();
					} else if (result.Kind == TEvResultType.resString) {
						result.strResult = Res1.strResult + Res2.strResult;
					} else if (result.Kind == TEvResultType.resBool) {
						result = ErrorCreate(String.format(SqrExpCannotOperator, SqrExpAdd, SqrExpBoolean));
					}
				} else if (FOpCode == TEvOperator.opMinus) {
					if(result.Kind == TEvResultType.resInt) {
						result.intResult = Res1.intResult - Res2.intResult;
					} else if(result.Kind == TEvResultType.resDouble){
						result.dblResult = BigDecimal.valueOf(Res1.dblResult).subtract(BigDecimal.valueOf(Res2.dblResult)).doubleValue() ;
					} else if (result.Kind == TEvResultType.resString) {
						result = ErrorCreate(String.format(SqrExpCannotOperator, SqrExpSubtract, SqrExpString));
					} else if (result.Kind == TEvResultType.resBool) {
						result = ErrorCreate(String.format(SqrExpCannotOperator, SqrExpSubtract, SqrExpBoolean));
					}
				} else if (FOpCode == TEvOperator.opMul) {
					if(result.Kind == TEvResultType.resInt) {
						result.intResult = Res1.intResult * Res2.intResult;
					} else if(result.Kind == TEvResultType.resDouble){
//						result.dblResult = Res1.dblResult * Res2.dblResult;
						result.dblResult = BigDecimal.valueOf(Res1.dblResult).multiply(BigDecimal.valueOf(Res2.dblResult)).setScale(64, BigDecimal.ROUND_HALF_UP).doubleValue();
					} else if (result.Kind == TEvResultType.resString) {
						result = ErrorCreate(String.format(SqrExpCannotOperator, SqrExpMultiply, SqrExpString));
					} else if (result.Kind == TEvResultType.resBool) {
						result = ErrorCreate(String.format(SqrExpCannotOperator, SqrExpMultiply, SqrExpBoolean));
					}
				} else if (FOpCode == TEvOperator.opDiv) {
					if (result.Kind == TEvResultType.resInt) {
						if (Res2.intResult != 0) {
//							result.dblResult = (double)Res1.intResult / (double)Res2.intResult;
							result.dblResult = new BigDecimal((double)Res1.intResult).divide(new BigDecimal((double)Res2.intResult),64,BigDecimal.ROUND_HALF_UP).doubleValue();
							result.Kind = TEvResultType.resDouble;
						} else {
							result = ErrorCreate(SqrExpDivideByZero);
						}
					} else if (result.Kind == TEvResultType.resDouble) {
						if (Res2.dblResult != 0) {
//							result.dblResult = Res1.dblResult / Res2.dblResult;
							result.dblResult = BigDecimal.valueOf(Res1.dblResult).divide(BigDecimal.valueOf(Res2.dblResult),64,BigDecimal.ROUND_HALF_UP).doubleValue();
						} else {
							result = ErrorCreate(SqrExpDivideByZero);
						}
					} else if (result.Kind == TEvResultType.resString) {
						result = ErrorCreate(String.format(SqrExpCannotOperator, SqrExpDivide, SqrExpString));
					} else if (result.Kind == TEvResultType.resBool) {
						result = ErrorCreate(String.format(SqrExpCannotOperator, SqrExpDivide, SqrExpBoolean));
					}
				} else if (FOpCode == TEvOperator.opGreater) {
					result.Kind = TEvResultType.resBool;
					if (Res1.Kind == TEvResultType.resInt) {
						result.booResult = Res1.intResult > Res2.intResult;
					} else if (Res1.Kind == TEvResultType.resDouble) {
						result.booResult = Res1.dblResult > Res2.dblResult;
					} else if (Res1.Kind == TEvResultType.resString) {
						//result.booResult = Res1.strResult > Res2.strResult;
						if(Res1.strResult.compareTo(Res2.strResult)>0){
							result.booResult = true;
						}
					} else if (Res1.Kind == TEvResultType.resBool) {
						result = ErrorCreate(String.format(SqrExpOperatorNotCompatible, ">", SqrExpBoolean));
					}
				} else if (FOpCode == TEvOperator.opGreaterOrEqual) {
					result.Kind = TEvResultType.resBool;
					if (Res1.Kind == TEvResultType.resInt) {
						result.booResult = Res1.intResult >= Res2.intResult;
					} else if (Res1.Kind == TEvResultType.resDouble) {
						result.booResult = Res1.dblResult >= Res2.dblResult;
					} else if (Res1.Kind == TEvResultType.resString) {
						//result.booResult = Res1.strResult >= Res2.strResult;
						if(Res1.strResult.compareTo(Res2.strResult)>=0){
							result.booResult = true;
						}
					} else if (Res1.Kind == TEvResultType.resBool) {
						result = ErrorCreate(String.format(SqrExpOperatorNotCompatible, ">=", SqrExpBoolean));
					}
				} else if (FOpCode == TEvOperator.opLess) {
					result.Kind = TEvResultType.resBool;
					if (Res1.Kind == TEvResultType.resInt) {
						result.booResult = Res1.intResult < Res2.intResult;
					} else if (Res1.Kind == TEvResultType.resDouble) {
						result.booResult = Res1.dblResult < Res2.dblResult;
					} else if (Res1.Kind == TEvResultType.resString) {
						//result.booResult = Res1.strResult < Res2.strResult;
						if(Res1.strResult.compareTo(Res2.strResult)<0){
							result.booResult = true;
						}
					} else if (Res1.Kind == TEvResultType.resBool) {
						result = ErrorCreate(String.format(SqrExpOperatorNotCompatible, "<", SqrExpBoolean));
					}
				} else if (FOpCode == TEvOperator.opLessOrEqual) {
					result.Kind = TEvResultType.resBool;
					if (Res1.Kind == TEvResultType.resInt) {
						result.booResult = Res1.intResult <= Res2.intResult;
					} else if (Res1.Kind == TEvResultType.resDouble) {
						result.booResult = Res1.dblResult <= Res2.dblResult;
					} else if (Res1.Kind == TEvResultType.resString) {
						//result.booResult = Res1.strResult <= Res2.strResult;
						if(Res1.strResult.compareTo(Res2.strResult)<=0){
							result.booResult = true;
						}
					} else if (Res1.Kind == TEvResultType.resBool) {
						result = ErrorCreate(String.format(SqrExpOperatorNotCompatible, "<=", SqrExpBoolean));
					}
				} else if (FOpCode == TEvOperator.opEqual) {
					result.Kind = TEvResultType.resBool;
					if (Res1.Kind == TEvResultType.resInt) {
						result.booResult = Res1.intResult == Res2.intResult;
					} else if (Res1.Kind == TEvResultType.resDouble) {
						result.booResult = Res1.dblResult == Res2.dblResult;
					} else if (Res1.Kind == TEvResultType.resString) {
						result.booResult = Res1.strResult.equals(Res2.strResult) ;
					} else if (Res1.Kind == TEvResultType.resBool) {
						result.booResult = Res1.booResult == Res2.booResult;
					}
				} else if (FOpCode == TEvOperator.opUnequal) {
					result.Kind = TEvResultType.resBool;
					if (Res1.Kind == TEvResultType.resInt) {
						result.booResult = Res1.intResult != Res2.intResult;
					} else if (Res1.Kind == TEvResultType.resDouble) {
						result.booResult = Res1.dblResult != Res2.dblResult;
					} else if (Res1.Kind == TEvResultType.resString) {
						result.booResult = (!Res1.strResult.equals(Res2.strResult));
					} else if (Res1.Kind == TEvResultType.resBool) {
						result.booResult = Res1.booResult != Res2.booResult;
					}
				} else if (FOpCode == TEvOperator.opOr) {
					result.Kind = Res1.Kind;
					if (Res1.Kind == TEvResultType.resInt) {
						result.intResult = Res1.intResult | Res2.intResult;
					} else if (Res1.Kind == TEvResultType.resDouble) {
						result = ErrorCreate(String.format(SqrExpOperatorNotCompatible, "OR", SqrExpNumeric));
					} else if (Res1.Kind == TEvResultType.resString) {
						result = ErrorCreate(String.format(SqrExpOperatorNotCompatible, "OR", SqrExpString));
					} else if (Res1.Kind == TEvResultType.resBool) {
						result.booResult = Res1.booResult || Res2.booResult;
					}
				} else if (FOpCode == TEvOperator.opXor) {
					result.Kind = Res1.Kind;
					if (Res1.Kind == TEvResultType.resInt) {
						result.intResult = Res1.intResult ^ Res2.intResult;
					} else if (Res1.Kind == TEvResultType.resDouble) {
						result = ErrorCreate(String.format(SqrExpOperatorNotCompatible, "XOR", SqrExpNumeric));
					} else if (Res1.Kind == TEvResultType.resString) {
						result = ErrorCreate(String.format(SqrExpOperatorNotCompatible, "XOR", SqrExpString));
					} else if (Res1.Kind == TEvResultType.resBool) {
						result.booResult = Res1.booResult ^ Res2.booResult;
					}
				} else if (FOpCode == TEvOperator.opAnd) {
					result.Kind = Res1.Kind;
					if (Res1.Kind == TEvResultType.resInt) {
						result.intResult = Res1.intResult & Res2.intResult;
					} else if (Res1.Kind == TEvResultType.resDouble) {
						result = ErrorCreate(String.format(SqrExpOperatorNotCompatible, "AND", SqrExpNumeric));
					} else if (Res1.Kind == TEvResultType.resString) {
						result = ErrorCreate(String.format(SqrExpOperatorNotCompatible, "AND", SqrExpString));
					} else if (Res1.Kind == TEvResultType.resBool) {
						result.booResult = Res1.booResult && Res2.booResult;
					}
				}
			} else if (Res1.Kind == TEvResultType.resError) {
				result = Res1;
			} else {
				result = Res2;
			}
			return result;
		}
	}

	public class TEvElementString extends TEvElement {
		private String FValue;
		public TEvElementString(String Value) {
			FValue = Value;
		}
		public TEvResult Value(TFiFo FiFo) {
			TEvResult result = new TEvResult();
			result.Kind = TEvResultType.resString;
			result.strResult = FValue;
			return result;
		}
	}

	public class TEvElementWrapper extends TEvElement {
		private TEvElement FEmbeddedFunction;
		public TEvElement getFEmbeddedFunction() {
			return FEmbeddedFunction;
		}

		public TEvElementWrapper(TEvElement AEmbeddedFunction) {
			FEmbeddedFunction = AEmbeddedFunction;
		}
		public TEvResult Value(TFiFo FiFo) {
			return FEmbeddedFunction.Value(FiFo);
		}
	}

	public class TEvEmbeddedFunction extends TEvElement {
		private TEvaluator Evaluator = new TEvaluator();
		private String FExpression;
		private boolean FInEvaluate = false;

		public TEvEmbeddedFunction(String Expression){
			FExpression = Expression;
		}

		public String Expression() {
			return FExpression;
		}

		public TEvElement Peek(int Index) {
			TEvElement result = null;
			if (Evaluator.isFPrepared() && Index <= Evaluator.FiFo.FiFo.size()) {
				result = (TEvElement)Evaluator.FiFo.FiFo.get(Index);
			}
			return result;
		}

		public TEvResult Value(TFiFo FiFo) {
			TEvResult result = null;
			if (!FInEvaluate) {
				FInEvaluate = true;
				result = Evaluator.Value();
				FInEvaluate = false;
			} else {
				result = ErrorCreate(SqrExpRecursive);
			}
			return result;
		}
	}

	public class TEvResultClass {
		public TEvResult EvResult;
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	public class TEvEnvironment {
		private int PrepareCount;
		private List OwnDataSets;
		private List FDatasets;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public class TFiFo {
		private boolean FAggreg = false;
		public boolean isFAggreg() {
			return FAggreg;
		}
		public void setFAggreg(boolean fAggreg) {
			FAggreg = fAggreg;
		}
		private List FiFo;
		private int FNextItem = 0;

		public TFiFo() {
			FiFo = new ArrayList();
		}

		public Object Get() {
			Object result = null;
			if(FNextItem < FiFo.size()) {
				result = FiFo.get(FNextItem);
				FNextItem++;
			}
			return result;
		}

		public Object GetAndFree() {
			Object result = null;
			if(FiFo.size() > 0) {
				result = FiFo.get(0);
				FiFo.remove(0);
			}
			return result;
		}

		public void Put(Object Value) {
			FiFo.add(Value);
		}

		public void Start() {
			FNextItem = 0;
		}
	}

	public class TFunctionLibrary extends TLibrary {

		public TEvElement GetFunction(String Name) {
			TEvElement result = null;
			TEvElementFunction AObject; //TEvElementFunctionClass
			TLibraryEntry aLibraryEntry;

			if (Entries.containsKey(Name)) {
				aLibraryEntry = (TLibraryEntry)Entries.get(Name);
//				aObject := TEvElementFunctionClass(aLibraryEntry.Item);
//		        result := aObject.Create;
				aLibraryEntry.getFItem().ArgString = "";
				aLibraryEntry.getFItem().CalcLog = "";
				AObject = (TEvElementFunction)aLibraryEntry.getFItem();
				result = AObject;
			} else {
				result = new TEvElementError(String.format(SqrExpUnknownFunction, Name));
			}
			return result;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public class TLibrary {
		protected HashMap Entries;
		public HashMap getEntries() {
			return Entries;
		}
		public void setEntries(HashMap entries) {
			Entries = entries;
		}

		public TLibrary() {
			Entries = new HashMap();
		}

		protected TLibraryEntry GetEntry(int Index) {
			TLibraryEntry result;
			if(Index < Entries.size()) {
				result = (TLibraryEntry) Entries.get(Index);
			} else {
				result = null;
			}
			return result;
		}

		public void Add(TEvElementFunction aItem, String AName, String ADescription, String AVendor, String AData) {//TLibraryItemClass
			TLibraryEntry aLibraryEntry = new TLibraryEntry();
			aLibraryEntry.setFName(AName);
			aLibraryEntry.setFDescription(ADescription);
			aLibraryEntry.setFVendor(AVendor);
			aLibraryEntry.setFData(AData);
			aLibraryEntry.setFItem(aItem);
			Entries.put(AName.toUpperCase(), aLibraryEntry);
		}
	}

	public class TLibraryEntry {

		public TLibraryEntry() {
		}

		private String FDescription;
		public String getFDescription() {
			return FDescription;
		}
		public void setFDescription(String fDescription) {
			FDescription = fDescription;
		}

		private String FData;
		public String getFData() {
			return FData;
		}
		public void setFData(String fData) {
			FData = fData;
		}

		private TEvElementFunction FItem;//TLibraryItemClass
		public TEvElementFunction getFItem() {
			return FItem;
		}
		public void setFItem(TEvElementFunction fItem) {
			FItem = fItem;
		}

		private String FName;
		public String getFName() {
			return FName;
		}
		public void setFName(String fName) {
			FName = fName;
		}

		private String FVendor;
		public String getFVendor() {
			return FVendor;
		}
		public void setFVendor(String fVendor) {
			FVendor = fVendor;
		}
	}
	/**
	 * 公式计算
	 * @param hmList
	 * @param sExpr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public TResult CalcExprWithLog(HashMap hmList, String sExpr) {
		TEvResult AResult;
		String TextResult = "";
		sExpr=sExpr.trim();
		TResult result = new TResult();

		LogStr = null;
		CalcLogStr = "";
		try {
			result.isOK = false;
			//GlobalEnvironment.Prepare;
			TEvaluator tEvaluator = new TEvaluator();
			tEvaluator.FDataSets = hmList;
			AResult = tEvaluator.CalculateAndLog(TrimExpr(sExpr));
			if(AResult.Kind == TEvResultType.resInt) {
				TextResult = Integer.toString(AResult.intResult);
				result.Kind = TEvResultType.resInt;
			} else if (AResult.Kind == TEvResultType.resDouble) {
				if(configFieldnames.containsKey(key)) {
					Vwsysfieldcfg expressionconfigs = (Vwsysfieldcfg) configFieldnames.get(key);
					String datafmt = expressionconfigs.getData_fmt()==null?"":expressionconfigs.getData_fmt();
					if(!"".equals(datafmt.trim())) {
						String scale = datafmt.split("\\.", -1)[1];
						if(scale != null) {
							String tmpResultStr = String.valueOf(AResult.dblResult);
							BigDecimal b = new BigDecimal(tmpResultStr);
							TextResult = b.setScale(scale.length(), BigDecimal.ROUND_HALF_UP).toString();
						} else {
							DecimalFormat df = new DecimalFormat(datafmt);
							TextResult = df.format(AResult.dblResult);
						}
					} else {
						String tmpResultStr = String.valueOf(AResult.dblResult);
						BigDecimal b = new BigDecimal(tmpResultStr);
						TextResult = b.setScale(4, BigDecimal.ROUND_HALF_UP).toString();
					}
				} else {
					String tmpResultStr = String.valueOf(AResult.dblResult);
					BigDecimal b = new BigDecimal(tmpResultStr);
					TextResult = b.setScale(4, BigDecimal.ROUND_HALF_UP).toString();
				}
				result.Kind = TEvResultType.resDouble;
			} else if (AResult.Kind == TEvResultType.resString) {
				TextResult = AResult.strResult;
				result.Kind = TEvResultType.resString;
			} else if (AResult.Kind == TEvResultType.resBool) {
				if (AResult.booResult) TextResult = "True";
				else TextResult = "False";
				result.Kind = TEvResultType.resBool;
			}
			if (AResult.Kind == TEvResultType.resError) {
				TextResult = AResult.strResult;
			} else {
				result.isOK = true;
			}

			result.Value = TextResult;
			result.CalcLogStr = CalcLogStr;
		} catch (Exception ex) {
			ex.printStackTrace();
			result.Value = ex.getMessage();
			result.isException = true;
//
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public TResult Calc_Expr(HashMap hmList , String sExpr) {
		TEvResult AResult;
		String TextResult = "";
		TResult result = new TResult();

		try {
			result.isOK = false;
			//GlobalEnvironment.Prepare;
			TEvaluator tEvaluator = new TEvaluator();
			tEvaluator.FDataSets = hmList;
			AResult = tEvaluator.Calculate(TrimExpr(sExpr));
			if(AResult.Kind == TEvResultType.resInt) {
				TextResult = Integer.toString(AResult.intResult);
			} else if (AResult.Kind == TEvResultType.resDouble) {
				TextResult = Double.toString((AResult.dblResult));
			} else if (AResult.Kind == TEvResultType.resString) {
				TextResult = AResult.strResult;
			} else if (AResult.Kind == TEvResultType.resBool) {
				if (AResult.booResult) TextResult = "True";
				else TextResult = "False";
			} else if (AResult.Kind == TEvResultType.resError) {
				TextResult = AResult.strResult;
			}

			result.Value = TextResult;
			result.isOK = true;
		} catch (Exception ex) {
			result.Value = ex.getMessage();
			result.isException = true;
		}
		return result;
	}

	public TEvResult ErrorCreate(String Value) {
		TEvResult result = new TEvResult();
		result.setKind(TEvResultType.resError);
		result.setStrResult(Value);
		return result;
	}

	public boolean EvResultToBoolean(TEvResult AValue) throws Exception {
		boolean result = false;
		if (AValue.Kind == TEvResultType.resBool) {
			result = AValue.booResult;
		} else if (AValue.Kind == TEvResultType.resString) {
			if ("TRUE".equalsIgnoreCase(AValue.strResult)) {
				result = true;
			} else if ("FALSE".equalsIgnoreCase(AValue.strResult)) {
				result = false;
			} else {
				throw new Exception(String.format(SqrNotValue, SqrExpBoolean));
			}
		} else {
			throw new Exception(String.format(SqrNotValue, SqrExpBoolean));
		}
		return result;
	}

	public double EvResultToFloat(TEvResult AValue) throws Exception {
		double result;
		if (AValue.Kind == TEvResultType.resInt) {
			result = AValue.intResult;
		} else if (AValue.Kind == TEvResultType.resDouble) {
			result = AValue.dblResult;
		} else {
			throw new Exception(String.format(SqrNotValue, SqrExpNumeric));
		}
		return result;
	}

	public int EvResultToInt(TEvResult AValue) throws Exception {
		int result = 0;
		if (AValue.Kind == TEvResultType.resInt) {
			result = AValue.intResult;
		} else if (AValue.Kind == TEvResultType.resDouble) {
			result = (int) AValue.dblResult;
		} else {
			throw new Exception(String.format(SqrNotValue, SqrExpNumeric));
		}
		return result;
	}

	public String EvResultToString(TEvResult AValue) {
		String result = null;
		if (AValue.Kind == TEvResultType.resString) {
			result = AValue.strResult;
		} else if (AValue.Kind == TEvResultType.resInt) {
			result = Integer.toString(AValue.intResult);
		} else if (AValue.Kind == TEvResultType.resDouble) {
			result = Double.toString(AValue.dblResult);
		} else if (AValue.Kind == TEvResultType.resBool) {
			if(AValue.booResult) result = SqrTrue;
			else  result = SqrFalse;
		} else {
			//throw new Exception(String.format(SqrExpError, AValue.strResult));
		}
		return result;
	}

	public Object EvResultToVariant(TEvResult AValue) throws Exception {
		Object result = null;
		if (AValue.Kind == TEvResultType.resString) {
			result = AValue.strResult;
		} else if (AValue.Kind == TEvResultType.resInt) {
			result = AValue.intResult;
		} else if (AValue.Kind == TEvResultType.resDouble) {
			result = AValue.dblResult;
		} else if (AValue.Kind == TEvResultType.resBool) {
			result = AValue.booResult;
		} else {
			throw new Exception(String.format(SqrExpError, AValue.strResult));
		}
		return result;
	}

	public String Flip(String aString, char a, char b) {
		int ParLevel = 0;
		boolean isString = false;
		int I = 0;
		char aChar;
		while (I < aString.length()) {
			aChar = aString.charAt(I);
			if ("'".equals(Character.toString(aChar))) {
				isString = true;
			} else if (!isString) {
				if("(".equals(Character.toString(aChar))) {
					ParLevel++;
				} else if (")".equals(Character.toString(aChar))) {
					ParLevel--;
				} else {
					if (ParLevel == 0) {
						if (aChar == a) {
							//aString = aString.replace(aChar, b);
							aString = aString.substring(0, I) + b + aString.substring(I + 1, aString.length());
						} else if (aChar == b) {
							//aString = aString.replace(aChar, a);
							aString = aString.substring(0, I) + a + aString.substring(I + 1, aString.length());
						}
					}
				}
			}
			I++;
		}
		return aString;
	}

	public String TrimExpr(String AExpr) {
		AExpr.replaceAll("�? ", ">=");
		AExpr.replaceAll("�? ", "<=");
		AExpr.replaceAll("\r", "");
		return AExpr;
	}

	public void RegisterFunction(TEvElementFunction FunctionClass,String Name,String Description,String Vendor,String Arguments) {
		FunctionLibrary.Add(FunctionClass, Name, Description, Vendor, Arguments);
	}

	public void RegisterFunctionEx(TEvElementFunction FunctionClass) {//TLibraryItemClass
//		 with TEvElementFunctionClass(FunctionClass).Create do
//			    try
//			        RegisterFunction(FunctionClass, FunctionName, FunctionDescription, FunctionVendor, FunctionArguments);
//			    finally
//			        free;
//			    end;
		String FunctionName = FunctionClass.FunctionName();
		String FunctionDescription = FunctionClass.FunctionDescription();
		String FunctionVendor = FunctionClass.FunctionVendor();
		String FunctionArguments = FunctionClass.FunctionArguments();

		RegisterFunction(FunctionClass, FunctionName, FunctionDescription, FunctionVendor, FunctionArguments);
	}

	public class TEvElementFunctionClass {
		public TEvElementFunctionClass() {
		}
	}

	public class TLibraryItemClass {
		public TLibraryItemClass() {
		}
	}

	public enum TEvOperator {
		opLess, opLessOrEqual, opGreater, opGreaterOrEqual, opEqual,opUnequal,
		opPlus, opMinus, opOr, opMul, opDiv, opAnd, opXor
	}

	public class TEvResult {
		public TEvResult() {

		}

		private TEvResultType Kind;
		public TEvResultType getKind() {
			return Kind;
		}
		public void setKind(TEvResultType kind) {
			Kind = kind;
		}

		private int intResult;
		public int getIntResult() {
			return intResult;
		}
		public void setIntResult(int intResult) {
			this.intResult = intResult;
		}

		private double dblResult;
		public double getDblResult() {
			return dblResult;
		}
		public void setDblResult(double dblResult) {
			this.dblResult = dblResult;
		}

		private String strResult;
		public String getStrResult() {
			return strResult;
		}
		public void setStrResult(String strResult) {
			this.strResult = strResult;
		}

		private boolean booResult;
		public boolean isBooResult() {
			return booResult;
		}
		public void setBooResult(boolean booResult) {
			this.booResult = booResult;
		}

		private Object ObjResult;
		public Object getObjResult() {
			return ObjResult;
		}
		public void setObjResult(Object objResult) {
			ObjResult = objResult;
		}

	}

	public static class TResult {
		public boolean isOK = false;
		public boolean isException = false;
		public String Value = "";
		public TEvResultType Kind;
		public String CalcLogStr = "";
	}

	public enum TEvResultType {
		resInt, resDouble, resString, resBool, resError, resObject
	}

	private static String ArgSeparator = ",";
	TFunctionLibrary FunctionLibrary = new TFunctionLibrary();
	private String LogStr;
	private String CalcLogStr = "";
	public String getCalcLogStr() {
		return CalcLogStr;
	}

	//计算时，错误提示配置
	private static String SqrExpRecursive = "Recursive calls not allowed";
	private static String SqrEvalNotPrepared = "Evaluator not prepared";
	private static String SqrExpMissing = "Missing %s";
	private static String SqrInvalidNot = "Invalid use of NOT";
	private static String SqrExpIllegalCharInNumeric = "Illegal character in numeric contant '%s'";
	private static String SqrQuSoft = "A Lochert";
	private static String SqrExpOperatorNotCompatible = "Operator %s is not compatible with %s expressions";
	private static String SqrExpDivideByZero = "Cannot divide by 0";
	private static String SqrExpAdd = "add";
	private static String SqrExpSubtract = "subtract";
	private static String SqrExpMultiply = "multiply";
	private static String SqrExpDivide = "divide";
	private static String SqrExpCannotOperator = "Cannot %s %s expressions";
	private static String SqrExpUnknownFunction = "Unknown function : %s";
	private static String SqrExpBoolean = "boolean";
	private static String SqrExpString = "string";
	private static String SqrExpNumeric = "numeric";
	private static String SqrNotValue = "Not a %s value";
	private static String SqrExpError = "Error in expression : %s";
	private static String SqrTrue = "True";
	private static String SqrFalse = "False";


	public Share_Expr() {
		functionfactory=new ValFunctionFactory();
		
		RegisterFunctionEx(new TEvGetAccountTypeFunction());
		RegisterFunctionEx(new TEvGetSubStringFunction());
		RegisterFunctionEx(new TEvGetIndexOfFunction());
		RegisterFunctionEx(new TEvGetLengthFunction());
		RegisterFunctionEx(new TEvGetStringReplaceFunction());
		RegisterFunctionEx(new TEvGetBondcodeFunction());
		RegisterFunctionEx(new TEvGetBondmarketFunction());
		RegisterFunctionEx(new TEvGetAssetAdtypeFunction());
		RegisterFunctionEx(new TEvToNumberFunction());
		RegisterFunctionEx(new TEvGetMatcherFunction());
		RegisterFunctionEx(new TEvGetConcatFunction());
		
		RegisterFunctionEx(new TEvToStringFunction());
		
		RegisterFunctionEx(new TEvGetMatcherFindFunction());
		RegisterFunctionEx(new TEvGetMatcherNotFindFunction());
	}
	
	
	public class TEvGetAccountTypeFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 1 && Argument(0).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resString;
				try {
					result.strResult ="";// functionfactory.GetAccountType(Argument(0).strResult);
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			}
			else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetAccountType(<t8_deal_info_id>)|获取账户类型";
		}

		public String FunctionName() {
			return "GetAccountType";
		}
	}
	
	
	public class TEvGetSubStringFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			
			if("".equals(Argument(0).strResult)) {
				result.Kind = TEvResultType.resString;
				result.strResult="";
				return result;
			}
			if (ArgList.size() == 2 && Argument(0).Kind == TEvResultType.resString&& Argument(1).Kind == TEvResultType.resInt){
				result.Kind = TEvResultType.resString;
				try {
					result.strResult=Argument(0).strResult.substring(Argument(1).intResult);
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			}else if(ArgList.size() == 3 && Argument(0).Kind == TEvResultType.resString && Argument(1).Kind == TEvResultType.resInt
					&& Argument(2).Kind == TEvResultType.resInt) {
				result.Kind = TEvResultType.resString;
				//微服务
				if(Argument(0).strResult.length()>=Argument(2).intResult) {
					result.strResult=Argument(0).strResult.substring(Argument(1).intResult,Argument(2).intResult);
				}else {
					result.strResult=Argument(0).strResult;
				}

			}
			else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetSubString(<t8_deal_info_id>)|获取账户类型";
		}

		public String FunctionName() {
			return "GetSubString";
		}
	}
	
	
	public class TEvGetIndexOfFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			
			if("".equals(Argument(0).strResult)) {
				result.Kind = TEvResultType.resInt;
				result.intResult=-1;
				return result;
			}
			if (ArgList.size() == 2 && Argument(0).Kind == TEvResultType.resString&& Argument(1).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resInt;
				try {
					result.intResult=Argument(0).strResult.indexOf(Argument(1).strResult);
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			} else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetIndexOf(<t8_deal_info_id>)|获取账户类型";
		}

		public String FunctionName() {
			return "GetIndexOf";
		}
	}
	
	
	public class TEvGetLengthFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 1 && Argument(0).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resInt;
				try {
					result.intResult=Argument(0).strResult.length();
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			} else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetLength(<t8_deal_info_id>)|获取账户类型";
		}

		public String FunctionName() {
			return "GetLength";
		}
	}
	
	public class TEvGetStringReplaceFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 3 && Argument(0).Kind == TEvResultType.resString&& Argument(1).Kind == TEvResultType.resString && Argument(2).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resString;
				result.strResult=Argument(0).strResult.replaceAll(Argument(1).strResult,Argument(2).strResult);//
			}
			else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetStringReplace(<t8_deal_info_id>)|获取账户类型";
		}

		public String FunctionName() {
			return "GetStringReplace";
		}
	}
	
	
	public class TEvGetBondcodeFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 1 && Argument(0).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resString;
				try {
					result.strResult=functionfactory.GetBondcode(Argument(0).strResult);
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			} else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetBondcode(<t8_deal_info_id>)|获取债券代码";
		}

		public String FunctionName() {
			return "GetBondcode";
		}
	}
	
	public class TEvGetBondmarketFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 1 && Argument(0).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resString;
				try {
					result.strResult=functionfactory.GetBondmarket(Argument(0).strResult);
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			} else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetBondmarket(<t8_deal_info_id>)|获取债券代码";
		}

		public String FunctionName() {
			return "GetBondmarket";
		}
	}
	
	
	public class TEvGetAssetAdtypeFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 1 && Argument(0).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resString;
				try {
					result.strResult=functionfactory.GetAssetAdtype(Argument(0).strResult);
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			} else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetAssetAdtype(<t8_deal_info_id>)|获取资产品种";
		}

		public String FunctionName() {
			return "GetAssetAdtype";
		}
	}
	
	
	public class TEvGetProdbaseidFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 1 && Argument(0).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resString;
				try {
					result.strResult=functionfactory.GetProdbaseid(Argument(0).strResult);
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			} else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetProdbaseid(<产品代码>)|获取产品ID";
		}

		public String FunctionName() {
			return "GetProdbaseid";
		}
	}

	
	
	public class TEvGetPortfolidFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 1 && Argument(0).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resString;
				try {
					result.strResult=functionfactory.GetPortfolid(Argument(0).strResult);
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			} else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetPortfolid(<投组代码>)|获取投组ID";
		}

		public String FunctionName() {
			return "GetPortfolid";
		}
	}
	
	/**
	 * 转数字
	 * @author zhangzl
	 *
	 */
	public class TEvToNumberFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 1 && Argument(0).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resDouble;
				try {
					result.dblResult=Double.parseDouble((Argument(0).strResult));
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			} else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "ToNumber(<字符串>)";
		}

		public String FunctionName() {
			return "ToNumber";
		}
	}
	
	/**
	 * 转数字
	 * @author zhangzl
	 *
	 */
	public class TEvToStringFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 1){
				result.Kind = TEvResultType.resString;
				try {
					result.strResult=Argument(0).ObjResult!=null?Argument(0).ObjResult.toString():"";;
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			}
			else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "ToString(<字符串>)";
		}

		public String FunctionName() {
			return "ToString";
		}
	}
	
	/**
	 * 正则表达式找配置字段合并数据
	 * @author zhangzl
	 *
	 */
	public class TEvGetMatcherFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 2 && Argument(0).Kind == TEvResultType.resString&& Argument(1).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resString;
				try {
					Pattern pattern = Pattern.compile(Argument(1).strResult);//正则表达式参数
					Matcher matcher = pattern.matcher(Argument(0).strResult);//正则表达式字符
					String strResult="";
					while(matcher.find()) {
					//System.out.println(matcher.group(1));
						strResult+=matcher.group();
					}
					result.strResult=strResult;
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			} else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetMatcher(<字符串>)";
		}

		public String FunctionName() {
			return "GetMatcher";
		}
	}
	
	
	/**
	 * 字符拼装
	 * @author Lenovo
	 *
	 */
	public class TEvGetConcatFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 2 && Argument(0).Kind == TEvResultType.resString&& Argument(1).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resString;
				try {
					String str1 = Argument(0).strResult;
					String str2 = Argument(1).strResult;
					String strResult=""+str1+str2;
					
					result.strResult=strResult;
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			} else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetConcat(<字符串>)";
		}

		public String FunctionName() {
			return "GetConcat";
		}
	}
	
	
	/**
	 * 正则表达式查找返回true/false
	 * @author zhangzl
	 *
	 */
	public class TEvGetMatcherFindFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 2 && Argument(0).Kind == TEvResultType.resString&& Argument(1).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resBool;
				try {
					Pattern pattern = Pattern.compile(Argument(1).strResult);//正则表达式参数
					Matcher matcher = pattern.matcher(Argument(0).strResult);//正则表达式字符
					//String strResult=matcher.find()?"true":"false";
					result.booResult=matcher.find();
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			} else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetMatcherFind(<字符串>)";
		}

		public String FunctionName() {
			return "GetMatcherFind";
		}
	}
	
	
	/**
	 * 正则表达式查找返回true/false
	 * @author zhangzl
	 *
	 */
	public class TEvGetMatcherNotFindFunction extends TEvElementFunction {
		public TEvResult Calculate() {
			TEvResult result = new TEvResult();
			if (ArgList.size() == 2 && Argument(0).Kind == TEvResultType.resString&& Argument(1).Kind == TEvResultType.resString){
				result.Kind = TEvResultType.resBool;
				try {
					Pattern pattern = Pattern.compile(Argument(1).strResult);//正则表达式参数
					Matcher matcher = pattern.matcher(Argument(0).strResult);//正则表达式字符
					//String strResult=matcher.find()?"true":"false";
					result.booResult=matcher.find()?false:true;
				} catch (Exception ex) {
					ex.printStackTrace();
					result.Kind = TEvResultType.resError;
					result.strResult=ex.getMessage();
				}
			} else{
				result.Kind= TEvResultType.resError;
				result.strResult="参数错误";
			}
			return result;
		}

		public String FunctionArguments() {
			return "N";
		}

		public String FunctionDescription() {
			return "GetMatcherNotFind(<字符串>)";
		}

		public String FunctionName() {
			return "GetMatcherNotFind";
		}
	}
}
