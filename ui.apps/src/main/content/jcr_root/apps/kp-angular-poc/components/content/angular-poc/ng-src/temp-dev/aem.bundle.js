//!function(n){function e(t){if(l[t])return l[t].exports;var o=l[t]={i:t,l:!1,exports:{}};return n[t].call(o.exports,o,o.exports,e),o.l=!0,o.exports}var l={};e.m=n,e.c=l,e.i=function(n){return n},e.d=function(n,l,t){e.o(n,l)||Object.defineProperty(n,l,{configurable:!1,enumerable:!0,get:t})},e.n=function(n){var l=n&&n.__esModule?function(){return n.default}:function(){return n};return e.d(l,"a",l),l},e.o=function(n,e){return Object.prototype.hasOwnProperty.call(n,e)},e.p="",e(e.s=58)}({58:function(n,e){window.AEM=window.AEM||{},window.AEM.templates=window.AEM.templates||{},window.AEM.templates["hello-world.component"]=function(n,e){return n.ɵvid(0,[(e()(),n.ɵeld(0,null,null,1,"h1",[],null,null,null,null,null)),(e()(),n.ɵted(null,["Hello World! Here"])),(e()(),n.ɵted(null,["\n"]))],null,null)}}});
//# sourceMappingURL=aem.bundle.js.map


window.AEM = window.AEM || {};

window.AEM['ng-app'] = {
  render : function(l, import1) {
    return import1.ɵvid(0, [
      (l()(), import1.ɵeld(0, null, null, 1, 'hello-world', [[
        'data-aeminstanceid',
        'instanceA'
      ]
      ], null, null, null, import2.View_HelloWorldComponent_0, import2.RenderType_HelloWorldComponent)),
      import1.ɵdid(114688, null, 0, import3.HelloWorldComponent, [], { aemId: [
        0,
        'aemId'
      ]
      }, null),
      (l()(), import1.ɵted(null, ['\n            '])),
      (l()(), import1.ɵeld(0, null, null, 1, 'hello-world', [[
        'data-aeminstanceid',
        'instanceB'
      ]
      ], null, null, null, import2.View_HelloWorldComponent_0, import2.RenderType_HelloWorldComponent)),
      import1.ɵdid(114688, null, 0, import3.HelloWorldComponent, [], { aemId: [
        0,
        'aemId'
      ]
      }, null)
    ], function (ck, v) {
      var currVal_0 = 'instanceA';
      ck(v, 1, 0, currVal_0);
      var currVal_1 = 'instanceB';
      ck(v, 4, 0, currVal_1);
    }, null);
  }
};

window.AEM['hello-world'] = {
  checkInstance : function (ck,v) {
    var co = v.component;
    var currVal_0 = (co.aemInstanceId == 'instanceA');
    ck(v, 1, 0, currVal_0);
    var currVal_1 = (co.aemInstanceId == 'instanceB');
    ck(v, 4, 0, currVal_1);
  },
  instance1 : function(l, import1) { return [
    (l()(), import1.ɵeld(0, null, null, 17, 'div', [], null, null, null, null, null)),
    (l()(), import1.ɵted(null, ['\n    '])),
    (l()(), import1.ɵeld(0, null, null, 1, 'h1', [], null, null, null, null, null)),
    (l()(), import1.ɵted(null, [
      'Instance1 - Hello World! Here - ',
      ''
    ])),
    (l()(), import1.ɵted(null, ['\n    '])),
    (l()(), import1.ɵeld(0, null, null, 1, 'p', [], null, null, null, null, null)),
    (l()(), import1.ɵted(null, ['Other Content'])),
    (l()(), import1.ɵted(null, ['\n    '])),
    import1.ɵncd(null, 0),
    (l()(), import1.ɵted(null, ['\n    '])),
    (l()(), import1.ɵeld(0, null, null, 1, 'h2', [], null, null, null, null, null)),
    (l()(), import1.ɵted(null, [
      'Also ',
      ''
    ])),
    (l()(), import1.ɵted(null, ['\n    '])),
    (l()(), import1.ɵeld(0, null, null, 1, 'h2', [], null, null, null, null, null)),
    (l()(), import1.ɵted(null, ['Test3 - AEM CONTENT FOR INSTANCE 1'])),
    (l()(), import1.ɵted(null, ['\n    '])),
    import1.ɵncd(null, 1),
    (l()(), import1.ɵted(null, ['\n']))
  ]},
  instance2 : function(l, import1) { return [
    (l()(), import1.ɵeld(0, null, null, 17, 'div', [], null, null, null, null, null)),
    (l()(), import1.ɵted(null, ['\n    '])),
    (l()(), import1.ɵeld(0, null, null, 1, 'h1', [], null, null, null, null, null)),
    (l()(), import1.ɵted(null, [
      'Instance2 - Hello World! Here - ',
      ''
    ])),
    (l()(), import1.ɵted(null, ['\n    '])),
    (l()(), import1.ɵeld(0, null, null, 1, 'p', [], null, null, null, null, null)),
    (l()(), import1.ɵted(null, ['Other Content'])),
    (l()(), import1.ɵted(null, ['\n    '])),
    import1.ɵncd(null, 0),
    (l()(), import1.ɵted(null, ['\n    '])),
    (l()(), import1.ɵeld(0, null, null, 1, 'h2', [], null, null, null, null, null)),
    (l()(), import1.ɵted(null, [
      'Also ',
      ''
    ])),
    (l()(), import1.ɵted(null, ['\n    '])),
    (l()(), import1.ɵeld(0, null, null, 1, 'h2', [], null, null, null, null, null)),
    (l()(), import1.ɵted(null, ['Test3 - AEM CONTENT FOR INSTANCE 2'])),
    (l()(), import1.ɵted(null, ['\n    '])),
    import1.ɵncd(null, 1),
    (l()(), import1.ɵted(null, ['\n']))
  ]}
};


