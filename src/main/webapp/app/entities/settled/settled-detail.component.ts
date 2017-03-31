import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {Settled} from "./settled.model";
import {SettledService} from "./settled.service";

@Component({
	selector: 'jhi-settled-detail',
	templateUrl: './settled-detail.component.html'
})
export class SettledDetailComponent implements OnInit, OnDestroy {

	settled: Settled;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private settledService: SettledService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['settled']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.settledService.find(id).subscribe(settled => {
			this.settled = settled;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
