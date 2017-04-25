import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {LeavingDestination} from "./leaving-destination.model";
import {LeavingDestinationService} from "./leaving-destination.service";

@Component({
	selector: 'jhi-leaving-destination-detail',
	templateUrl: './leaving-destination-detail.component.html'
})
export class LeavingDestinationDetailComponent implements OnInit, OnDestroy {

	leavingDestination: LeavingDestination;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private leavingDestinationService: LeavingDestinationService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['leavingDestination']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.leavingDestinationService.find(id).subscribe(leavingDestination => {
			this.leavingDestination = leavingDestination;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
