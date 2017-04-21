import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {EthnicOrigin} from "./ethnic-origin.model";
import {EthnicOriginService} from "./ethnic-origin.service";

@Component({
	selector: 'jhi-ethnic-origin-detail',
	templateUrl: './ethnic-origin-detail.component.html'
})
export class EthnicOriginDetailComponent implements OnInit, OnDestroy {

	ethnicOrigin: EthnicOrigin;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private ethnicOriginService: EthnicOriginService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['ethnicOrigin']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.ethnicOriginService.find(id).subscribe(ethnicOrigin => {
			this.ethnicOrigin = ethnicOrigin;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
