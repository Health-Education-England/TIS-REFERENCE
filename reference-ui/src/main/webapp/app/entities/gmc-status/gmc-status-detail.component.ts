import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {GmcStatus} from "./gmc-status.model";
import {GmcStatusService} from "./gmc-status.service";

@Component({
	selector: 'jhi-gmc-status-detail',
	templateUrl: './gmc-status-detail.component.html'
})
export class GmcStatusDetailComponent implements OnInit, OnDestroy {

	gmcStatus: GmcStatus;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gmcStatusService: GmcStatusService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['gmcStatus']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.gmcStatusService.find(id).subscribe(gmcStatus => {
			this.gmcStatus = gmcStatus;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
