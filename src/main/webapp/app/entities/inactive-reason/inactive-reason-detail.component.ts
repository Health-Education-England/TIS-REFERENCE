import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {InactiveReason} from "./inactive-reason.model";
import {InactiveReasonService} from "./inactive-reason.service";

@Component({
	selector: 'jhi-inactive-reason-detail',
	templateUrl: './inactive-reason-detail.component.html'
})
export class InactiveReasonDetailComponent implements OnInit, OnDestroy {

	inactiveReason: InactiveReason;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private inactiveReasonService: InactiveReasonService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['inactiveReason']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.inactiveReasonService.find(id).subscribe(inactiveReason => {
			this.inactiveReason = inactiveReason;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
