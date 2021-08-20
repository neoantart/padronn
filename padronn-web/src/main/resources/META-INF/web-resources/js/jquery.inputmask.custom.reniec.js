/**
 * Definiciones personalizadas de jquery.inputmask
 * https://github.com/RobinHerbots/jquery.inputmask
 *
 * User: lmamani
 * Date: 25/07/13
 * Time: 08:21 PM
 * To change this template use File | Settings | File Templates.
 */
$(function () {

	$.extend($.inputmask.defaults.definitions, {
		'w': {
            validator: function (chrs, buffer, pos, strict, opts) {
                //var re=/^[a-zA-ZÑÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕäëïöüáéíóúàèìòùâêîôûãõñ](?!.+ {2})[a-zA-ZÑÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕäëïöüáéíóúàèìòùâêîôûãõñ\'0-9 ]*$/gm,
				var re=/^[a-zA-ZÑÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕäëïöüáéíóúàèìòùâêîôûãõñ](?!.+ {2})[a-zA-ZÑÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕäëïöüáéíóúàèìòùâêîôûãõñ\' ]*$/gm,
                    cbuffer = buffer.slice(),
                    bufferStr;
                cbuffer.splice(pos, 0, chrs);
                bufferStr = cbuffer.join('');
                return re.test(bufferStr);
            },
			cardinality: 1,
			casing: "upper"
		},
        'e':{
            validator: function (chrs, buffer, pos, strict, opts) {
                var re=/^(?!.+ {2})[a-zA-ZÑÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕäëïöüáéíóúàèìòùâêîôûãõñ\'0-9\, ]*$/gm,
                    cbuffer = buffer.slice(),
                    bufferStr;
                cbuffer.splice(pos, 0, chrs);
                bufferStr = cbuffer.join('');
                return re.test(bufferStr);
            },
            cardinality: 1,
            casing: "upper"
        },
        'k':{
            //validator:"[ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕabcdefghijklmnñopqrstuvwxyzäëïöüáéíóúàèìòùâêîôûãõ\\,\\-\\' \\.0123456789]",
            validator: function (chrs, buffer, pos, strict, opts) {
                var re=/^[ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕabcdefghijklmnñopqrstuvwxyzäëïöüáéíóúàèìòùâêîôûãõ\,\-\' \.0123456789](?!.+ {2})[ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕabcdefghijklmnñopqrstuvwxyzäëïöüáéíóúàèìòùâêîôûãõ\,\-\' \.0123456789 ]*$/gm,
                    cbuffer = buffer.slice(),
                    bufferStr;
                cbuffer.splice(pos, 0, chrs);
                bufferStr = cbuffer.join('');
                return re.test(bufferStr);
            },
            /*validator: function (chrs, buffer, pos, strict, opts) {
                var re = new RegExp("^[ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕabcdefghijklmnñopqrstuvwxyzäëïöüáéíóúàèìòùâêîôûãõ\\,\\-\\' \\.0123456789]*$"),
                    cbuffer = buffer.slice(),
                    doubleSpace = "  ",
                    noFirstChars = "-.' ",
                    bufferStr;
                cbuffer.splice(pos, 0, chrs);
                bufferStr = cbuffer.join('');
                return re.test(bufferStr) && !(bufferStr.indexOf(doubleSpace) != -1) && !(noFirstChars.indexOf(bufferStr[0]) != -1);
            },*/
            cardinality: 1,
            casing: "upper"
        },
        'g':{
            /*validator:"[ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz\\, ]",*/
            validator: function (chrs, buffer, pos, strict, opts) {
                var re=/^[ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz](?!.+ {2})[ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz\' ]*$/gm,
                    cbuffer = buffer.slice(),
                    bufferStr;
                cbuffer.splice(pos, 0, chrs);
                bufferStr = cbuffer.join('');
                return re.test(bufferStr);
            },
            /*validator: function (chrs, buffer, pos, strict, opts) {
                var re = new RegExp("^[ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz\\, ]*$"),
                    cbuffer = buffer.slice(),
                    doubleSpace = "  ",
                    doubleComma = ",,",
                    doubleCommaSpace = ", ,",
                    noFirstChars = ", ",
                    bufferStr;
                cbuffer.splice(pos, 0, chrs);
                bufferStr = cbuffer.join('');
                return re.test(bufferStr) && !(bufferStr.indexOf(doubleSpace) != -1) && !(bufferStr.indexOf(doubleComma) != -1) && !(bufferStr.indexOf(doubleCommaSpace) != -1) && !(noFirstChars.indexOf(bufferStr[0]) != -1);
            },*/
            cardinality: 1,
            casing: "upper"
        },
		'u': {
			//validator: "[0-9A-Za-z]",
			validator: "[0-9]",
			cardinality: 1,
			casing: "upper"
		},
		'@': {
			validator: "[0-9A-Za-z_\\@\\-\\.]",
			cardinality: 1
		},
		'.': {
			validator: ".",
			cardinality: 1
		},
		'#': {
			validator: "[ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕabcdefghijklmnñopqrstuvwxyzäëïöüáéíóúàèìòùâêîôûãõ\\-\\' \\.0123456789]",
            /*validator: function (chrs, buffer, pos, strict, opts) {
                var re = new RegExp("^[ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕabcdefghijklmnñopqrstuvwxyzäëïöüáéíóúàèìòùâêîôûãõ\\-\\' \\.0123456789,]*$"),
                    cbuffer = buffer.slice(),
                    noFirstChars = "-' .0123456789",
                    bufferStr;
                cbuffer.splice(pos, 0, chrs);
                bufferStr = cbuffer.join('');
//                console.log(bufferStr);
                return re.test(bufferStr); //&& !(bufferStr.indexOf(doubleSpace)!=-1) && !(noFirstChars.indexOf(bufferStr[0]) != -1);
            },*/
			cardinality: 1,
			casing: "upper"
		}
        /*,
        'x':{<
            validator:function(chrs, buffer, pos, strict, opts){
                var valExp2 = new RegExp("[^1234567890]{1}|[ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕabcdefghijklmnñopqrstuvwxyzäëïöüáéíóúàèìòùâêîôûãõ\\-\\' \\.0123456789]{1,40}");
                if(valExp2.test(buffer)) {
                    return true;
                }
            },
            cardinality: 40,
            casing:"upper",
            prevalidator: null,
            placeholder: ""
        }*/
	});


	$.extend($.inputmask.defaults.aliases, {
		'dd/mm/yyyy': {
			mask: "1/2/y",
			placeholder: " ",
			regex: {
				val1pre: new RegExp("[0-3]"), //daypre
				val1: new RegExp("0[1-9]|[12][0-9]|3[01]"), //day
				val2pre: function (separator) { var escapedSeparator = $.inputmask.escapeRegex.call(this, separator); return new RegExp("((0[1-9]|[12][0-9]|3[01])" + escapedSeparator + "[01])"); }, //monthpre
				val2: function (separator) { var escapedSeparator = $.inputmask.escapeRegex.call(this, separator); return new RegExp("((0[1-9]|[12][0-9])" + escapedSeparator + "(0[1-9]|1[012]))|(30" + escapedSeparator + "(0[13-9]|1[012]))|(31" + escapedSeparator + "(0[13578]|1[02]))"); }//month
			},
            validator: "",
			leapday: "29/02/",
			separator: '/',
			yearrange: { minyear: 1900, maxyear: new Date().getFullYear()*1 },
			isInYearRange: function (chrs, minyear, maxyear) {
				var enteredyear = parseInt(chrs.concat(minyear.toString().slice(chrs.length)));
				var enteredyear2 = parseInt(chrs.concat(maxyear.toString().slice(chrs.length)));
				return (enteredyear != NaN ? minyear <= enteredyear && enteredyear <= maxyear : false) ||
						(enteredyear2 != NaN ? minyear <= enteredyear2 && enteredyear2 <= maxyear : false);
			},
			determinebaseyear: function (minyear, maxyear) {
				var currentyear = (new Date()).getFullYear();
				if (minyear > currentyear) return minyear;
				if (maxyear < currentyear) return maxyear;

				return currentyear;
			},
			onKeyUp: function (e, buffer, opts) {
				var $input = $(this);
				if (e.ctrlKey && e.keyCode == opts.keyCode.RIGHT) {
					var today = new Date();
					$input.val(today.getDate().toString() + (today.getMonth() + 1).toString() + today.getFullYear().toString());
				}
//                console.log(buffer);
                if($input.val().trim().length == 10) { // fecha completa

                    var nowTemp = new Date();
                    var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
                    var datePart = $input.val().split('/');
                    var dateInput = new Date(datePart[2], datePart[1]*1-1, datePart[0], 0, 0, 0, 0);
                    if(dateInput.valueOf() > now.valueOf()) {
                        alert('La fecha no puede ser futura');
                        $input.val('').focus();
                        return;
                    }
                }
			},
			definitions: {
				'1': { //val1 ~ day or month
					validator: function (chrs, buffer, pos, strict, opts) {
						var isValid = opts.regex.val1.test(chrs);
						if (!strict && !isValid) {
							if (chrs.charAt(1) == opts.separator || "-./".indexOf(chrs.charAt(1)) != -1) {
								isValid = opts.regex.val1.test("0" + chrs.charAt(0));
								if (isValid) {
									buffer[pos - 1] = "0";
									return { "pos": pos, "c": chrs.charAt(0) };
								}
							}
						}
						return isValid;
					},
					cardinality: 2,
					prevalidator: [{
						validator: function (chrs, buffer, pos, strict, opts) {
							var isValid = opts.regex.val1pre.test(chrs);
							if (!strict && !isValid) {
								isValid = opts.regex.val1.test("0" + chrs);
								if (isValid) {
									buffer[pos] = "0";
									pos++;
									return { "pos": pos };
								}
							}
							return isValid;
						}, cardinality: 1
					}]
				},
				'2': { //val2 ~ day or month
					validator: function (chrs, buffer, pos, strict, opts) {
						var frontValue = buffer.join('').substr(0, 3);
						var isValid = opts.regex.val2(opts.separator).test(frontValue + chrs);
						if (!strict && !isValid) {
							if (chrs.charAt(1) == opts.separator || "-./".indexOf(chrs.charAt(1)) != -1) {
								isValid = opts.regex.val2(opts.separator).test(frontValue + "0" + chrs.charAt(0));
								if (isValid) {
									buffer[pos - 1] = "0";
									return { "pos": pos, "c": chrs.charAt(0) };
								}
							}
						}
						return isValid;
					},
					cardinality: 2,
					prevalidator: [{
						validator: function (chrs, buffer, pos, strict, opts) {
							var frontValue = buffer.join('').substr(0, 3);
							var isValid = opts.regex.val2pre(opts.separator).test(frontValue + chrs);
							if (!strict && !isValid) {
								isValid = opts.regex.val2(opts.separator).test(frontValue + "0" + chrs);
								if (isValid) {
									buffer[pos] = "0";
									pos++;
									return { "pos": pos };
								}
							}
							return isValid;
						}, cardinality: 1
					}]
				},
				'y': { //year
					validator: function (chrs, buffer, pos, strict, opts) {
						if (opts.isInYearRange(chrs, opts.yearrange.minyear, opts.yearrange.maxyear)) {
							var dayMonthValue = buffer.join('').substr(0, 6);
							if (dayMonthValue != opts.leapday)
								return true;
							else {
								var year = parseInt(chrs, 10);//detect leap year
								if (year % 4 === 0)
									if (year % 100 === 0)
										if (year % 400 === 0)
											return true;
										else return false;
									else return true;
								else return false;
							}
						} else return false;
					},
					cardinality: 4,
					prevalidator: [
						{
							validator: function (chrs, buffer, pos, strict, opts) {
								var isValid = opts.isInYearRange(chrs, opts.yearrange.minyear, opts.yearrange.maxyear);
								strict = true;
								if (!strict && !isValid) {
									var yearPrefix = opts.determinebaseyear(opts.yearrange.minyear, opts.yearrange.maxyear).toString().slice(0, 1);

									isValid = opts.isInYearRange(yearPrefix + chrs, opts.yearrange.minyear, opts.yearrange.maxyear);
									if (isValid) {
										buffer[pos++] = yearPrefix[0];
										return { "pos": pos };
									}
									yearPrefix = opts.determinebaseyear(opts.yearrange.minyear, opts.yearrange.maxyear).toString().slice(0, 2);

									isValid = opts.isInYearRange(yearPrefix + chrs, opts.yearrange.minyear, opts.yearrange.maxyear);
									if (isValid) {
										buffer[pos++] = yearPrefix[0];
										buffer[pos++] = yearPrefix[1];
										return { "pos": pos };
									}
								}
								return isValid;
							},
							cardinality: 1
						},
						{
							validator: function (chrs, buffer, pos, strict, opts) {
								var isValid = opts.isInYearRange(chrs, opts.yearrange.minyear, opts.yearrange.maxyear);
								strict = true;
								if (!strict && !isValid) {
									var yearPrefix = opts.determinebaseyear(opts.yearrange.minyear, opts.yearrange.maxyear).toString().slice(0, 2);

									isValid = opts.isInYearRange(yearPrefix + chrs[1], opts.yearrange.minyear, opts.yearrange.maxyear);
									if (isValid) {
										buffer[pos++] = yearPrefix[1];
										return { "pos": pos };
									}

									yearPrefix = opts.determinebaseyear(opts.yearrange.minyear, opts.yearrange.maxyear).toString().slice(0, 2);
									if (opts.isInYearRange(yearPrefix + chrs, opts.yearrange.minyear, opts.yearrange.maxyear)) {
										var dayMonthValue = buffer.join('').substr(0, 6);
										if (dayMonthValue != opts.leapday)
											isValid = true;
										else {
											var year = parseInt(chrs, 10);//detect leap year
											if (year % 4 === 0)
												if (year % 100 === 0)
													if (year % 400 === 0)
														isValid = true;
													else isValid = false;
												else isValid = true;
											else isValid = false;
										}
									} else isValid = false;
									if (isValid) {
										buffer[pos - 1] = yearPrefix[0];
										buffer[pos++] = yearPrefix[1];
										buffer[pos++] = chrs[0];
										return { "pos": pos };
									}
								}
								return isValid;
							},
							cardinality: 2
						},
						{
							validator: function (chrs, buffer, pos, strict, opts) {
								return opts.isInYearRange(chrs, opts.yearrange.minyear, opts.yearrange.maxyear);
							},
							cardinality: 3
						}
					]
				}
			},
            autoUnmask: false
		},
        'apellido': {
			mask: "w",
			repeat: 60,
			placeholder: "",
            onKeyDown: function (e, buffer, opts) {
                var $input = $(this);
                /*$input.on('change', function(e){
                    $input.val($input.val().trim());
                });*/
            }
		},
		'nombre': {
			mask: "w",
			repeat: 60,
			placeholder: "",
            onKeyDown: function (e, buffer, opts) {
                var $input = $(this);
                /*$input.on('change', function(e){
                    $input.val($input.val().trim());
                });*/
            }
		},
		'dni': {
			mask: "u",
			repeat: 8,
			placeholder: ""
		},
		'cnv': {
			mask: "u",
			repeat: 10,
			placeholder: ""
		},
		'email': {
			mask: "@",
			repeat: 65,
			placeholder: ""
		},
		'direccion': {
			mask: "#",
			repeat: 100,
			placeholder: ""
		},
        'select2-input' : {
            mask: "k",
            repeat:100,
            placeholder: ""
        },
        'observacion':{
            mask:'#',
            repeat:200,
            placeholder: ""
        },
        'ubigeo':{
            mask:'g',
            repeat:100,
            placeholder:""
        },
        'telefono':{
            mask:'u',
            repeat:9,
            placeholder:""
        },
        'entidad':{
            mask:'e',
            repeat:300,
            placeholder:""
        }
	});
});
