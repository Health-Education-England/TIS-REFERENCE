import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {MaritalStatus} from "./marital-status.model";
import {MaritalStatusService} from "./marital-status.service";

@Component({
	selector: 'jhi-marital-status-detail',
	templateUrl: './marital-status-detail.component.html'
})
export class MaritalStatusDetailComponent implements OnInit, OnDestroy {

	maritalStatus: MaritalStatus;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private maritalStatusService: MaritalStatusService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['maritalStatus']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.maritalStatusService.find(id).subscribe(maritalStatus => {
			this.maritalStatus = maritalStatus;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
