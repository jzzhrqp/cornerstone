发送：
  NotifyBean notifyBean = new NotifyBean("I come from Host","I am 2 years old");
        RxBus.getInstance().postResultMap(notifyBean);

接收：
      RxBus.getInstance().tObservable(NotifyBean.class).subscribe(new Consumer<NotifyBean>() {
            @Override
            public void accept(NotifyBean notifyBean) throws Exception {
                tvName.setText(notifyBean.name);
                tvAge.setText(notifyBean.age);
            }
        });


